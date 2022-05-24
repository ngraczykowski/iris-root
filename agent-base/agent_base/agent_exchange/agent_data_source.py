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
    """
    channel_stream_method attribute - it is a grpc channel stream call, given in implementation.
    I.e. in org name agent it is a NameInputServiceStub.BatchGetMatchNameInputs method
    """

    def __init__(self, config: Config, ssl: bool = False):
        assert config.application_config
        self.application_config = config.application_config
        self.address_service = AddressService(config.application_config)
        self.ssl = ssl
        self.channel, self.channel_stream_method = None, None
        self.logger = logging.getLogger("main").getChild("data_source")

    async def start(self):
        data_source_config = self.application_config["grpc"]["client"]["data-source"]
        address = await self.address_service.get(data_source_config["address"] or "")
        if not address:
            raise Exception("No address for data source")
        self.channel = grpc.aio.insecure_channel(address)
        if self.ssl:
            with open(data_source_config["client_ca"], "rb") as f:
                ca = f.read()
            with open(data_source_config["client_private_key"], "rb") as f:
                private_key = f.read()
            with open(data_source_config["client_public_key_chain"], "rb") as f:
                certificate_chain = f.read()
            server_credentials = grpc.ssl_channel_credentials(ca, private_key, certificate_chain)
            self.channel = grpc.aio.secure_channel(address, server_credentials)
        self.logger.info(f"Data source channel on {address}")
        self.channel_stream_method = None

    async def request(
        self, request: AgentExchangeRequest
    ) -> AsyncGenerator[Tuple[str, str, Any], None]:
        try:
            async for response in self.channel_stream_method(
                self.prepare_request(request),
                timeout=self.application_config["grpc"]["client"]["data-source"].get("timeout"),
            ):
                # https://www.python.org/dev/peps/pep-0525/#asynchronous-yield-from
                for parsed in self.parse_response(response):
                    yield parsed
        except grpc.RpcError as err:
            self.logger.warning(f"{err!r} for {request}")
            raise AgentDataSourceException()

    def prepare_request(self, request: AgentExchangeRequest) -> Any:
        raise NotImplementedError()

    def parse_response(self, response: Any) -> Generator[Tuple[str, str, Any], None, None]:
        raise NotImplementedError()

    async def stop(self) -> None:
        if self.channel:
            await self.channel.close()
            self.channel = None
