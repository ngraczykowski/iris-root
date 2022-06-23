import os
import ssl
import time
from typing import Dict, List

import lz4.frame
import pika
from pika.adapters.blocking_connection import BlockingChannel
from pika.delivery_mode import DeliveryMode
from pika.exchange_type import ExchangeType

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
        creds = pika.PlainCredentials(
            connection_configuration["login"],
            connection_configuration["password"],
        )
        if ssl_options:
            context = ssl.create_default_context(cafile=ssl_options["ca_certs"])
            context.load_cert_chain(ssl_options["certfile"], ssl_options["keyfile"])
            context.options |= ssl.OP_NO_TLSv1
            context.options |= ssl.OP_NO_TLSv1_1

            ssl_opts = pika.SSLOptions(context, connection_configuration["host"])
            conn_params = pika.ConnectionParameters(
                port=connection_configuration["port"],
                virtual_host=connection_configuration["virtualhost"],
                credentials=creds,
                ssl_options=ssl_opts,
            )
        else:
            conn_params = pika.ConnectionParameters(
                host=self.connection_configuration["host"],
                port=self.connection_configuration["port"],
                virtual_host=self.connection_configuration["virtualhost"],
                credentials=creds,
            )
        self.connection: pika.BlockingConnection = pika.BlockingConnection(conn_params)
        self.connection_configuration = connection_configuration
        self.channel: BlockingChannel = self.connection.channel()
        self.request_queue = self.channel.queue_declare(
            queue=self.messaging_configuration.get("queue-name", ""),
            durable=self.messaging_configuration.get("queue-durable", True),
            arguments=dict(self.messaging_configuration.get("queue-arguments", {})),
        )
        # logger.info(f"Got an existing (request) queue: {self.request_queue}")

        request_exchange = self.channel.exchange_declare(
            self.messaging_configuration.exchange,
            durable=self.messaging_configuration.get("queue-durable", True),
            exchange_type=ExchangeType.direct,
        )
        self.channel.queue_bind(
            queue=self.messaging_configuration.get("queue-name", ""),
            exchange=self.messaging_configuration.exchange,
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

    def on_request(self, body, properties) -> None:

        self.channel.basic_publish(
            exchange="bridge.historical",
            routing_key=self.messaging_configuration["routing-key"],
            body=body,
            properties=properties,
        )
        self.channel.confirm_delivery()
        return True


class HistoricalDecisionExchange:
    def __init__(self):
        self.connections: List[PikaConnection] = []
        self.service_config = ConsulServiceConfig()

    async def start(self):
        connection = await self._set_pika_connection()
        self.connections: List[PikaConnection] = [connection]

    def send_request(self, data):
        response_body = lz4.frame.compress(
            data,
            block_size=lz4.frame.BLOCKSIZE_MAX64KB,
            block_linked=False,
            compression_level=lz4.frame.COMPRESSIONLEVEL_MINHC,
            content_checksum=True,
            store_size=False,
        )
        logger.debug("Trying to send")
        from pika.spec import BasicProperties

        properties = BasicProperties(
            content_encoding="lz4",
            content_type="application/x-protobuf",
            delivery_mode=DeliveryMode.Persistent,
            headers={"springAutoDecompress": True},
            timestamp=int(time.time()),
            type="silenteight.learningstore.historicaldecision.v2.api.HistoricalDecisionLearningStoreExchangeRequest",
        )
        trials = 0
        result = False
        while trials < 10:
            try:
                result = self.connections[0].on_request(response_body, properties)
                break
            except Exception as e:
                trials += 1
                logger.debug(f"Trying again {str(e)}")
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
