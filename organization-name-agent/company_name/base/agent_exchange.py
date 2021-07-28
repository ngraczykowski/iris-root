import asyncio
import logging
import os
import time
from typing import Callable, Mapping

import aio_pika
import lz4.frame
from silenteight.agents.v1.api.exchange.exchange_pb2 import (
    AgentExchangeRequest,
    AgentExchangeResponse,
)

from company_name.base.exception import AgentException
from company_name.base.pika_connection import PikaConnection


class MessageFormatException(AgentException):
    retry = False


class AgentExchange:
    def __init__(
        self,
        config: Mapping,
        callback: Callable,
    ):
        self.config = config
        self.request_callback = callback
        self.connections = []
        self.logger = logging.getLogger("AgentExchange")

    async def start(self) -> None:
        self.logger.debug("Starting agent exchange")
        messaging_config = self.config["agent"]["messaging"]["agent-exchange"]
        for connection_config in self._prepare_connection_configurations():
            connection = PikaConnection(
                messaging_config, connection_config, self.on_request
            )
            try:
                await connection.start()
            except Exception as err:
                self.logger.info(
                    f"Unable to connect to queue on "
                    f" {connection_config.get('host', '-')}:{connection_config.get('port', '-')}"
                    f" ({err!r})"
                )
            else:
                self.connections.append(connection)
                break

        if not self.connections:
            raise Exception("No working pika connection")

        self.logger.debug("Agent exchange started")

    async def on_request(self, message: aio_pika.IncomingMessage) -> aio_pika.Message:
        body = message.body.strip()

        if message.content_encoding == "lz4":
            try:
                body = lz4.frame.decompress(body)
                self.logger.debug(f"message body {body!r}")
            except Exception:
                raise MessageFormatException(body)

        request: AgentExchangeRequest = AgentExchangeRequest.FromString(body)
        response: AgentExchangeResponse = await self.process(request)

        return aio_pika.Message(
            lz4.frame.compress(response.SerializeToString()),
            content_encoding="lz4",
            content_type="application/x-protobuf",
            delivery_mode=message.delivery_mode,
            headers=message.headers,
            priority=message.priority,
            timestamp=int(time.time()),
            type="silenteight.agents.v1.api.exchange.AgentExchangeResponse",
        )

    async def process(self, request: AgentExchangeRequest) -> AgentExchangeResponse:
        return self.request_callback(request)

    async def stop(self):
        self.logger.debug("Stopping agent exchange")
        await asyncio.gather(*(s.stop() for s in self.connections))
        self.connections = []
        self.logger.debug("Agent exchange stopped")

    def _prepare_connection_configurations(self):
        rabbitmq_config = self.config["rabbitmq"]

        # SPRING_RABBITMQ_USERNAME and SPRING_RABBITMQ_PASSWORD,
        #  cause why not made it framework agnostic...
        for config_key, environment_var in (
            ("login", "SPRING_RABBITMQ_USERNAME"),
            ("password", "SPRING_RABBITMQ_PASSWORD"),
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
