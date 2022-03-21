import asyncio
import collections
import logging
import os
import time
from typing import Any, AsyncGenerator, Dict, Generator, Tuple

import aio_pika
import lz4.frame
from google.protobuf.struct_pb2 import Struct
from silenteight.agents.v1.api.exchange.exchange_pb2 import (
    AgentExchangeRequest,
    AgentExchangeResponse,
    AgentOutput,
)

from agent_base.agent import AgentService
from agent_base.agent.exception import AgentException
from agent_base.agent_exchange.agent_data_source import (
    AgentDataSource,
    AgentDataSourceException,
)
from agent_base.agent_exchange.pika_connection import PikaConnection
from agent_base.utils import Config


class MessageFormatException(AgentException):
    retry = False


class AgentExchange(AgentService):
    data_source_error_solution = AgentOutput.FeatureSolution(solution="DATA_SOURCE_ERROR")
    default_error_solution = AgentOutput.FeatureSolution(solution="AGENT_ERROR")

    def __init__(self, config: Config, data_source: AgentDataSource):
        super().__init__(config)
        self.connections = []
        self.data_source = data_source
        self.logger = logging.getLogger("AgentExchange")

    async def start(self, *args, **kwargs) -> None:
        self.logger.debug("Starting agent exchange")

        await super().start(*args, **kwargs)
        connection = await self._set_pika_connection()
        self.connections = [connection]

        if self.data_source:
            await self.data_source.start()

        self.logger.info("Agent exchange started")

    async def stop(self):
        self.logger.info("Stopping agent exchange")

        tasks = [s.stop() for s in self.connections]
        if self.data_source:
            tasks.append(self.data_source.stop())

        await asyncio.gather(*tasks)
        self.connections = []

        self.logger.info("Agent exchange stopped")

    async def on_request(self, message: aio_pika.IncomingMessage) -> aio_pika.Message:
        body = message.body.strip()

        if message.content_encoding == "lz4":
            try:
                body = lz4.frame.decompress(body)
            except Exception:
                raise MessageFormatException(body)

        request: AgentExchangeRequest = AgentExchangeRequest.FromString(body)
        self.logger.info(f"Before request {message.headers}")
        response: AgentExchangeResponse = await self.process(request)
        self.logger.info(f"After request {message.headers}")

        response_body = lz4.frame.compress(
            response.SerializeToString(),
            block_size=lz4.frame.BLOCKSIZE_MAX64KB,
            block_linked=False,
            compression_level=lz4.frame.COMPRESSIONLEVEL_MINHC,
            content_checksum=True,
            store_size=False,
        )

        return aio_pika.Message(
            response_body,
            content_encoding="lz4",
            content_type="application/x-protobuf",
            delivery_mode=message.delivery_mode,
            headers=message.headers,
            priority=message.priority,
            timestamp=int(time.time()),
            type="silenteight.agents.v1.api.exchange.AgentExchangeResponse",
        )

    async def process(self, request: AgentExchangeRequest) -> AgentExchangeResponse:
        agent_inputs = self.data_source.request(request)
        resolved = await self._resolve_all(request, agent_inputs)
        response = self._prepare_response(request, resolved)
        return response

    async def _resolve_all(
        self, request: AgentExchangeRequest, agent_inputs: AsyncGenerator
    ) -> Dict[str, Dict[str, AgentOutput.Feature]]:
        resolved = collections.defaultdict(dict)
        try:
            self.logger.debug(type(agent_inputs))
            async for match, feature, task in self._create_tasks(agent_inputs):
                resolved[match][feature] = await self._resolve_task(feature, task)

        except AgentDataSourceException as err:
            self.logger.warning(repr(err))

        self._update_absent_solutions(
            request=request,
            resolved=resolved,
            solution=self.data_source_error_solution,
        )

        return resolved

    async def _create_tasks(
        self,
        request_inputs: AsyncGenerator[Generator[Tuple[str, str, Any], None, None], None],
    ) -> AsyncGenerator[Tuple[str, str, Any], None]:
        async for match, feature, args in request_inputs:
            yield match, feature, self.create_resolve_task(*args)

    async def _resolve_task(self, feature: str, task: Any):
        try:
            result = await task
            return self._create_agent_output_feature(feature, result)

        except Exception as err:
            self.logger.error(repr(err))
            return AgentOutput.Feature(
                feature=feature, feature_solution=self.default_error_solution
            )

    def _prepare_response(
        self, request: AgentExchangeRequest, resolved: Dict[str, Dict[str, Any]]
    ) -> AgentExchangeResponse:
        response = AgentExchangeResponse(agent_outputs=[])
        for match in request.matches:
            features = [
                resolved.get(match, {}).get(feature)
                or AgentOutput.Feature(
                    feature=feature, feature_solution=self.default_error_solution
                )
                for feature in request.features
            ]
            response.agent_outputs.append(AgentOutput(match=match, features=features))

        return response

    async def _set_pika_connection(self):
        messaging_config = self.config.application_config["agent"]["agent-exchange"]
        max_requests_to_worker = self.config.application_config["agent"]["processes"]
        for connection_config in self._prepare_connection_configurations():
            connection = PikaConnection(
                messaging_config, connection_config, self.on_request, max_requests_to_worker
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
                return connection

        raise Exception("No working pika connection")

    def _prepare_connection_configurations(self):
        rabbitmq_config = self.config.application_config["rabbitmq"]

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

    @staticmethod
    def _update_absent_solutions(
        request: AgentExchangeRequest,
        resolved: Dict[str, Dict[str, Any]],
        solution: AgentOutput.FeatureSolution,
    ):
        for match in request.matches:
            for feature in request.features:
                if feature not in resolved[match]:
                    resolved[match][feature] = AgentOutput.Feature(
                        feature=feature,
                        feature_solution=solution,
                    )

    @staticmethod
    def _create_agent_output_feature(feature: str, result: Tuple[Any, Any]):
        solution, reason = result
        reason_struct = Struct()
        reason_struct.update(reason)
        return AgentOutput.Feature(
            feature=feature,
            feature_solution=AgentOutput.FeatureSolution(solution=solution, reason=reason_struct),
        )
