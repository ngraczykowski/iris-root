import logging
from typing import Any, AsyncGenerator, Generator, Tuple

import grpc
from silenteight.agents.v1.api.exchange.exchange_pb2 import AgentExchangeRequest

from agent_base.agent.exception import AgentException
from agent_base.agent_exchange.address_service import AddressService
from agent_base.utils import Config


class AgentDataSourceException(AgentException):
    retry: True


class AgentDataSource:
    def __init__(self, config: Config):
        self.application_config = config.application_config
        self.address_service = AddressService(config.application_config)
        self.channel, self.command = None, None
        self.logger = logging.getLogger("AgentDataSource")

    async def start(self):
        address = await self.address_service.get(
            self.application_config["grpc"]["client"]["data-source"]["address"]
        )
        self.channel = grpc.aio.insecure_channel(address)
        self.command = None

    async def request(
        self, request: AgentExchangeRequest
    ) -> AsyncGenerator[Tuple[str, str, Any], None]:
        try:
            async for response in self.command(
                self.prepare_request(request),
                timeout=self.application_config["grpc"]["client"]["data-source"].get(
                    "timeout"
                ),
            ):
                # https://www.python.org/dev/peps/pep-0525/#asynchronous-yield-from
                for parsed in self.parse_response(response):
                    yield parsed
        except grpc.RpcError as err:
            self.logger.warning(f"{err!r} for {request}")
            raise AgentDataSourceException()

    def prepare_request(self, request: AgentExchangeRequest) -> Any:
        raise NotImplementedError()

    def parse_response(
        self, response: Any
    ) -> Generator[Tuple[str, str, Any], None, None]:
        raise NotImplementedError()

    async def stop(self) -> None:
        if self.channel:
            await self.channel.close()
            self.channel = None
