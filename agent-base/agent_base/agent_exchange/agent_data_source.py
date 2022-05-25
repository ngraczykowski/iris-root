import logging
import time
from typing import Any, AsyncGenerator, Generator, Tuple

import grpc
from silenteight.agents.v1.api.exchange.exchange_pb2 import AgentExchangeRequest

from agent_base.agent.exception import AgentException
from agent_base.agent_exchange.address_service import AddressService
from agent_base.utils import Config
from agent_base.utils.consul import ConsulServiceException


class AgentDataSourceException(AgentException):
    retry: True


class AgentDataSource:
    """
    channel_stream_method attribute - it is a grpc channel stream call, given in implementation.
    I.e. in org name agent it is a NameInputServiceStub.BatchGetMatchNameInputs method
    """

    def __init__(self, config: Config, ssl: bool = False):
        assert config.application_config
        self.config = config
        self.ssl = ssl
        self.channel, self.channel_stream_method, self.data_source_config = None, None, None
        self.logger = logging.getLogger("main").getChild("data_source")

    async def start(self):
        await self.connect_to_uds()
        self.prepare_stub()

    async def connect_to_uds(self):
        while True:
            try:
                self.config.reload()
                self.data_source_config = self.config.application_config["grpc"]["client"][
                    "data-source"
                ]
                address_service = AddressService(self.config.application_config)
                address = await address_service.get(self.data_source_config["address"])

                await self.initiate_channel(address)
                break

            except KeyError as err:
                self.logger.error(f"Configuration argument missing: {err}")
                raise

            except ConsulServiceException:
                self.logger.error("No UDS address in consul. Waiting 1s and trying again")
                time.sleep(1)

            except grpc.RpcError:
                self.logger.error("No UDS response. Waiting 1s and trying again")
                time.sleep(1)

        self.logger.info(f"Connected to UDS on {address}")

    async def initiate_channel(self, address):
        self.logger.info(f"Connecting to UDS via {address}")
        self.channel = grpc.aio.insecure_channel(address)
        if self.ssl:
            with open(self.data_source_config["client_ca"], "rb") as f:
                ca = f.read()
            with open(self.data_source_config["client_private_key"], "rb") as f:
                private_key = f.read()
            with open(self.data_source_config["client_public_key_chain"], "rb") as f:
                certificate_chain = f.read()
            server_credentials = grpc.ssl_channel_credentials(ca, private_key, certificate_chain)
            self.channel = grpc.aio.secure_channel(address, server_credentials)

        self.logger.info(f"Data source channel on {address}")

    async def request(
        self, request: AgentExchangeRequest
    ) -> AsyncGenerator[Tuple[str, str, Any], None]:
        try:
            async for response in self.channel_stream_method(
                self.prepare_request(request),
                timeout=self.data_source_config.get("timeout"),
            ):
                # https://www.python.org/dev/peps/pep-0525/#asynchronous-yield-from
                for parsed in self.parse_response(response):
                    yield parsed
        except grpc.RpcError as err:
            self.logger.warning(f"{err!r} for {request}")
            await self.start()
            self.request(request)

    def prepare_request(self, request: AgentExchangeRequest) -> Any:
        ...

    def parse_response(self, response: Any) -> Generator[Tuple[str, str, Any], None, None]:
        ...

    def prepare_stub(self):
        ...

    async def stop(self) -> None:
        if self.channel:
            await self.channel.close()
            self.channel = None
