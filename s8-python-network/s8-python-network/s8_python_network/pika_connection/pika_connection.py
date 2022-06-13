import logging
from typing import Callable

import aio_pika

from s8_python_network.pika_connection.config import MessagingConfig, RabbitMQConfig


class PikaConnection:
    def __init__(
        self,
        messaging_configuration: MessagingConfig,
        connection_configuration: RabbitMQConfig,
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
        self.logger = logging.getLogger("main").getChild("pika_connection")
        self.max_requests_to_worker = max_requests_to_worker
        self.ssl = use_ssl

    async def start(self) -> None:
        if self.ssl:
            if not self.connection_configuration.ssl_options:
                raise ValueError(
                    "No ssl connection parameters in config "
                    "- add 'rabbitmq.ssl_options' section to application.yaml"
                )
            url = "".join(
                map(
                    str,
                    (
                        "amqps://",
                        self.connection_configuration.login,
                        ":",
                        self.connection_configuration.password,
                        "@",
                        self.connection_configuration.host,
                        ":",
                        self.connection_configuration.port,
                        "/",
                        self.connection_configuration.virtualhost,
                        "?cafile=",
                        self.connection_configuration.ssl_options.cafile,
                        "&keyfile=",
                        self.connection_configuration.ssl_options.keyfile,
                        "&certfile=",
                        self.connection_configuration.ssl_options.certfile,
                        "&no_verify_ssl=",  # that's how aio-pika named it ...
                        self.connection_configuration.ssl_options.verify,
                    ),
                )
            )
            self.connection: aio_pika.RobustConnection = await aio_pika.connect_robust(url=url)
        else:
            self.connection: aio_pika.RobustConnection = await aio_pika.connect_robust(
                host=self.connection_configuration.host,
                port=self.connection_configuration.port,
                login=self.connection_configuration.login,
                password=self.connection_configuration.password,
                virtualhost=self.connection_configuration.virtualhost,
            )
        self.channel: aio_pika.Channel = await self.connection.channel()
        await self.channel.set_qos(prefetch_count=self.max_requests_to_worker)

        try:
            self.request_queue = await self.channel.get_queue(
                name=self.messaging_configuration.request.queue_name,
                ensure=True,
            )
            self.logger.info(f"Got an existing (request) queue: {self.request_queue}")
        except aio_pika.exceptions.ChannelNotFoundEntity as err:
            self.logger.debug(f"Queue doesn't exits: {err!r}")
            if self.messaging_configuration.request.exchange:

                # not sure why but on error close callbacks are called in aiormq,
                # and more exceptions happens
                await self.channel.close()
                channel: aio_pika.Channel = await self.connection.channel()

                self.request_queue: aio_pika.queue.Queue = await channel.declare_queue(
                    name=self.messaging_configuration.request.queue_name,
                    durable=self.messaging_configuration.request.queue_durable,
                    arguments=self.messaging_configuration.request.queue_arguments,
                )
                request_exchange = self.messaging_configuration.request.exchange
                await self.request_queue.bind(
                    exchange=request_exchange,
                    routing_key=self.messaging_configuration.request.routing_key,
                )
                self.logger.info(
                    f"Created queue {self.request_queue} and bind to exchange {request_exchange}"
                )
            else:
                raise

        self.callback_exchange = await self.channel.get_exchange(
            name=self.messaging_configuration.response.exchange,
            ensure=True,
        )
        self.logger.info(f"Got an existing (callback) exchange: {self.callback_exchange}")

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
        self.logger.debug(f"Received message id: {message.message_id}")

        try:
            response_message = await self.request_callback(message)
        except Exception as err:
            try:
                retry = err.retry
            except AttributeError:
                retry = True
            self.logger.warning(f"{err!r} on message id: {message.message_id}")
            await message.nack(requeue=retry)
            return

        await self.callback_exchange.publish(
            routing_key=self.messaging_configuration.response.routing_key,
            message=response_message,
        )
        await message.ack()
        self.logger.debug(f"acknowledged message id: {message.message_id}")
