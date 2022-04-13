import logging
import ssl
import sys
from typing import Callable, Dict

import aio_pika
import pika

from agent_base.agent.exception import AgentException


class PikaConnection:
    def __init__(
        self,
        messaging_configuration: Dict,
        connection_configuration: Dict,
        callback: Callable,
        max_requests_to_worker: int,
        use_ssl: bool = False,
    ):
        self.messaging_configuration = messaging_configuration
        self.connection_configuration = connection_configuration
        self.request_callback = callback
        (self.connection, self.request_queue, self.callback_exchange, self.request_queue_tag,) = (
            None,
            None,
            None,
            None,
        )
        self.logger = logging.getLogger("PikaConnection")
        c_handler = logging.StreamHandler(sys.stdout)
        self.logger.addHandler(c_handler)
        self.max_requests_to_worker = max_requests_to_worker
        self.ssl = use_ssl

    async def start(self) -> None:
        connection_configuration = self.connection_configuration.copy()
        ssl_options = connection_configuration.pop("tls", None)

        # TODO REFACTOR BELOW IF WORKS
        if self.ssl and ssl_options:
            context = ssl.SSLContext()
            context.verify_mode = ssl.CERT_REQUIRED
            context.load_verify_locations(ssl_options["ca_certs"])
            context.load_cert_chain(ssl_options["certfile"], ssl_options["keyfile"])
            ssl_options = pika.SSLOptions(context)
            credentials = pika.credentials.PlainCredentials(
                username=connection_configuration["login"],
                password=connection_configuration["password"],
            )
            connection_params = pika.ConnectionParameters(
                host=connection_configuration["host"],
                ssl_options=ssl_options,
                port=connection_configuration["port"],
                credentials=credentials,
                virtual_host=connection_configuration["virtualhost"],
            )
            rabbit = pika.BlockingConnection(connection_params)
            self.channel: pika.adapters.blocking_connection.BlockingChannel = rabbit.channel()
            self.channel.basic_qos(prefetch_count=self.max_requests_to_worker)
            queue_name = self.messaging_configuration["request"].get("queue-name", "")

            try:
                self.request_queue = self.channel.queue_declare(
                    self.messaging_configuration["request"].get("queue-name"),
                    passive=True,
                    durable=self.messaging_configuration["request"].get("queue-durable"),
                    arguments=self.messaging_configuration["request"].get("queue-arguments"),
                )

            except pika.adapters.blocking_connection.exceptions.ChannelClosed as err:
                self.logger.debug(f"Queue doesn't exits: {err!r}")
                if "exchange" in self.messaging_configuration["request"]:

                    # not sure why but on error close callbacks are called in aiormq,
                    # and more exceptions happens
                    self.channel.close()
                    channel: pika.adapters.blocking_connection.BlockingChannel = rabbit.channel()

                    self.request_queue = channel.queue_declare(
                        queue=queue_name,
                        durable=self.messaging_configuration["request"].get("queue-durable"),
                        arguments=self.messaging_configuration["request"].get("queue-arguments"),
                    )

                    channel.queue_bind(
                        queue=queue_name,
                        exchange=self.messaging_configuration["request"]["exchange"],
                        routing_key=self.messaging_configuration["request"].get("routing-key"),
                    )
                else:
                    raise

            response_exchange_conf = self.messaging_configuration["response"]
            self.callback_exchange = self.channel.exchange_declare(
                response_exchange_conf["exchange"],
                passive=True,
                # NOT sure if below needed as passive=True, so it's just checking if exchange exist
                # auto_delete=response_exchange_conf["exchange-auto-delete"],
                # durable=response_exchange_conf["exchange-durable"],
                # exchange_type=response_exchange_conf["exchange-type"],
                # internal=response_exchange_conf["exchange-auto-delete"],
            )
            self.request_queue_tag = self.channel.basic_consume(
                queue=queue_name,
                on_message_callback=lambda ch, method, properties, body: self.on_message_callback(
                    ch, method, properties, body
                ),
                auto_ack=False,
            )
            self.channel.start_consuming()

        else:
            self.connection: aio_pika.RobustConnection = await aio_pika.connect_robust(
                **connection_configuration
            )
            self.channel: aio_pika.Channel = await self.connection.channel()

            await self.channel.set_qos(prefetch_count=self.max_requests_to_worker)

            try:
                self.request_queue = await self.channel.get_queue(
                    name=self.messaging_configuration["request"].get("queue-name"),
                    ensure=True,
                )
            except aio_pika.exceptions.ChannelNotFoundEntity as err:
                self.logger.debug(f"Queue doesn't exits: {err!r}")
                if "exchange" in self.messaging_configuration["request"]:

                    # not sure why but on error close callbacks are called in aiormq,
                    # and more exceptions happens
                    await self.channel.close()
                    channel: aio_pika.Channel = await self.connection.channel()

                    self.request_queue = await channel.declare_queue(
                        name=self.messaging_configuration["request"].get("queue-name", ""),
                        durable=True,
                    )

                    await self.request_queue.bind(
                        exchange=self.messaging_configuration["request"]["exchange"],
                        routing_key=self.messaging_configuration["request"].get("routing-key"),
                    )
                else:
                    raise

            self.callback_exchange = await self.channel.get_exchange(
                self.messaging_configuration["response"]["exchange"]
            )
            self.request_queue_tag = await self.request_queue.consume(
                callback=self.on_request, no_ack=False
            )

    async def stop(self) -> None:
        # not sure whenever order is important, so doing it without gather
        if self.request_queue_tag:
            await self.request_queue.cancel(self.request_queue_tag)
        if self.channel:
            await self.channel.close()
        if self.connection:
            await self.connection.close()

    async def on_request(self, message: aio_pika.IncomingMessage) -> None:
        self.logger.debug(f"received {message}")
        try:
            response_message = await self.request_callback(message)
        except AgentException as err:
            self.logger.warning(f"{err!r} on {message}")
            message.nack(requeue=err.retry)
            return
        except Exception as err:
            self.logger.warning(f"{err!r} on {message}")
            message.nack()
            return
        await self.callback_exchange.publish(
            routing_key="",
            message=response_message,
        )
        message.ack()
        self.logger.debug(f"acknowledged {message.message_id}")

    async def on_message_callback(self, channel, method, properties, body):
        self.logger.debug(f"received properties {properties}")
        try:
            message = aio_pika.Message(
                body=body,
                delivery_mode=pika.spec.PERSISTENT_DELIVERY_MODE,
                headers=properties.headers,
                priority=properties.priority,
                content_encoding=properties.content_encoding,
            )
        except AttributeError as error:
            self.logger.error(f"Error when parsing message: {error}")
            return

        self.logger.debug(f"received {message}")
        try:
            response_message = await self.request_callback(message)
        except Exception as err:
            self.logger.warning(f"{err!r} on {message}")
            channel.basic_nack(delivery_tag=method.delivery_tag)
            return

        # TODO when you know which one works, remove non necessary one from below try-excepts
        _published = False
        try:
            self.channel.basic_publish(
                exchange=self.messaging_configuration["response"]["exchange"],
                routing_key=self.messaging_configuration["response"]["exchange-routing-key"],
                body=response_message.body,
                properties=pika.BasicProperties(
                    content_encoding=response_message.content_encoding,
                    content_type=response_message.content_type,
                    delivery_mode=response_message.delivery_mode,
                    headers=response_message.headers,
                    priority=response_message.priority,
                    timestamp=response_message.timestamp,
                    type=response_message.type,
                ),
            )
            _published = True
            self.logger.info("Published using self.channel")
        except Exception:
            self.logger.error("Error publishing using self.channel")

        try:
            if not _published:
                channel.basic_publish(
                    exchange=self.messaging_configuration["response"]["exchange"],
                    routing_key=self.messaging_configuration["response"]["exchange-routing-key"],
                    body=response_message.body,
                    properties=pika.BasicProperties(
                        content_encoding=response_message.content_encoding,
                        content_type=response_message.content_type,
                        delivery_mode=response_message.delivery_mode,
                        headers=response_message.headers,
                        priority=response_message.priority,
                        timestamp=response_message.timestamp,
                        type=response_message.type,
                    ),
                )
                self.logger.info("Published using channel from callback")
        except Exception:
            self.logger.error("Error publishing using channel from callback")

        try:
            self.channel.basic_ack(delivery_tag=method.delivery_tag)
        except Exception:
            self.logger.error("Error when ACK using self.channel")

        try:
            channel.basic_ack(delivery_tag=method.delivery_tag)
        except Exception:
            self.logger.error("Error when ACK using channel from callback args")

        self.logger.debug(f"acknowledged {response_message.headers}")
