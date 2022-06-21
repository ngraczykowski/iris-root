import os
import time
from typing import Dict, List

import aio_pika
import lz4.frame
import pamqp
from aio_pika import ExchangeType

from etl_pipeline.config import ConsulServiceConfig
from etl_pipeline.logger import get_logger

logger = get_logger("main", "ms_pipeline.log").getChild("Test")


class PikaConnection:
    def __init__(
        self,
        messaging_configuration: Dict,
        connection_configuration: Dict,
        use_ssl: bool = False,
    ):
        self.messaging_configuration = messaging_configuration
        self.connection_configuration = connection_configuration
        (self.connection, self.request_queue, self.callback_exchange, self.request_queue_tag,) = (
            None,
            None,
            None,
            None,
        )
        self.ssl = use_ssl

    async def start(self) -> None:
        connection_configuration = self.connection_configuration.copy()
        ssl_options = connection_configuration.pop("tls", None)

        if self.ssl:
            if not ssl_options:
                raise ValueError(
                    "No ssl connection parameters in config "
                    "- add 'rabbitmq.tls' section to application.yaml"
                )
            url = "".join(
                map(
                    str,
                    (
                        "amqps://",
                        connection_configuration["login"],
                        ":",
                        connection_configuration["password"],
                        "@",
                        connection_configuration["host"],
                        ":",
                        connection_configuration["port"],
                        "/",
                        connection_configuration["virtualhost"],
                        "?cafile=",
                        ssl_options["cafile"],
                        "&keyfile=",
                        ssl_options["keyfile"],
                        "&certfile=",
                        ssl_options["certfile"],
                        "&no_verify_ssl=",  # that's how aio-pika named it ...
                        ssl_options["verify"],
                    ),
                )
            )
            self.connection: aio_pika.RobustConnection = await aio_pika.connect(
                url=url, fail_fast=1
            )
        else:
            self.connection: aio_pika.RobustConnection = await aio_pika.connect(
                **connection_configuration
            )
        self.channel: aio_pika.Channel = await self.connection.channel(publisher_confirms=True)
        await self.channel.set_qos()

        try:
            self.request_queue = await self.channel.get_queue(
                name=self.messaging_configuration.get("queue-name"),
                ensure=True,
            )
            logger.info(f"Got an existing (request) queue: {self.request_queue}")
        except aio_pika.exceptions.ChannelNotFoundEntity as err:
            logger.debug(f"Queue doesn't exits: {err!r}")
            if "exchange" in self.messaging_configuration:
                await self.channel.close()
                self.channel: aio_pika.Channel = await self.connection.channel(
                    publisher_confirms=True
                )

                # not sure why but on error close callbacks are called in aiormq,
                # and more exceptions happens

                self.request_queue: aio_pika.queue.Queue = await self.channel.declare_queue(
                    name=self.messaging_configuration.get("queue-name", ""),
                    durable=self.messaging_configuration.get("queue-durable", True),
                    arguments=dict(self.messaging_configuration.get("queue-arguments", {})),
                )

            else:
                raise

        try:
            request_exchange = await self.channel.get_exchange(
                self.messaging_configuration.exchange
            )
        except:
            await self.channel.close()
            self.channel: aio_pika.Channel = await self.connection.channel(publisher_confirms=True)

            request_exchange = await self.channel.declare_exchange(
                self.messaging_configuration.exchange,
                ExchangeType.DIRECT,
            )
        self.request_queue: aio_pika.queue.Queue = await self.channel.declare_queue(
            name=self.messaging_configuration.get("queue-name", ""),
            durable=self.messaging_configuration.get("queue-durable", True),
            arguments=dict(self.messaging_configuration.get("queue-arguments", None)),
        )

        await self.request_queue.bind(
            exchange=request_exchange,
            routing_key=self.messaging_configuration.get("routing-key"),
        )
        logger.info(f"Created queue {self.request_queue} and bind to exchange {request_exchange}")

    async def stop(self) -> None:
        # not sure whenever order is important, so doing it without gather
        if self.request_queue_tag:
            await self.request_queue.cancel(self.request_queue_tag)
        if self.channel:
            await self.channel.close()
        if self.connection:
            await self.connection.close()

    async def on_request(self, response_message: aio_pika) -> None:
        exchange = await self.channel.get_exchange(self.messaging_configuration["exchange"])
        return await exchange.publish(
            routing_key=self.messaging_configuration["routing-key"],
            message=response_message,
            timeout=1,
        )


class HistoricalDecisionExchange:
    def __init__(self):
        self.connections: List[PikaConnection] = []
        self.service_config = ConsulServiceConfig()

    async def start(self):
        connection = await self._set_pika_connection()
        self.connections: List[PikaConnection] = [connection]

    async def send_request(self, data):
        response_body = lz4.frame.compress(
            data,
            block_size=lz4.frame.BLOCKSIZE_MAX64KB,
            block_linked=False,
            compression_level=lz4.frame.COMPRESSIONLEVEL_MINHC,
            content_checksum=True,
            store_size=False,
        )
        logger.debug("Trying to send")
        message = aio_pika.Message(
            response_body,
            content_encoding="lz4",
            content_type="application/x-protobuf",
            delivery_mode=aio_pika.DeliveryMode.PERSISTENT,
            headers={"springAutoDecompress": True},
            timestamp=int(time.time()),
            type="silenteight.learningstore.historicaldecision.v2.api.HistoricalDecisionLearningStoreExchangeRequest",
        )
        while True:
            try:
                result = await self.connections[0].on_request(message)
                break
            except:
                await self.start()
        result = isinstance(result, pamqp.commands.Basic.Ack)
        logger.debug(f"Result of publish: {'SUCCESS' if result else 'FAILURE'}")
        return result

    async def _set_pika_connection(self) -> PikaConnection:
        messaging_config = self.service_config.historical_decision_exchange

        while True:
            for connection_config in self._prepare_connection_configurations():
                connection = PikaConnection(messaging_config, connection_config)

                try:
                    await connection.start()
                except Exception as err:
                    logger.info(
                        f"Unable to connect to queue on "
                        f" {connection_config.get('host', '-')}:{connection_config.get('port', '-')}"
                        f" ({err!r})"
                    )
                else:
                    return connection
                logger.error("no working pika connection")
                time.sleep(5)

    def _prepare_connection_configurations(self):
        rabbitmq_config = self.service_config.rabbitmq

        for config_key, environment_var in (
            ("login", "RABBITMQ_USERNAME"),
            ("password", "RABBITMQ_PASSWORD"),
        ):
            if config_key not in rabbitmq_config and environment_var in os.environ:
                rabbitmq_config[config_key] = os.environ[environment_var]

        if "host" in rabbitmq_config:
            yield rabbitmq_config

        if "addresses" in rabbitmq_config:
            addresses = rabbitmq_config["addresses"]
            del rabbitmq_config["addresses"]
            for address in addresses.split(","):
                host, port = address.split(":")
                yield {"host": host, "port": port, **rabbitmq_config}
