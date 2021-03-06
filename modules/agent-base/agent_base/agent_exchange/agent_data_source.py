import logging
import time
from typing import Any, AsyncGenerator, Generator, Tuple

import grpc
from s8_python_network.consul import ConsulServiceError
from s8_python_network.grpc_channel import get_channel
from s8_python_network.ssl_credentials import SSLCredentials
from silenteight.agents.v1.api.exchange.exchange_pb2 import AgentExchangeRequest

from agent_base.agent.exception import AgentException
from agent_base.agent_exchange.address_service import AddressService
from agent_base.utils import Config, ConfigurationException
from agent_base.utils.config.application_config import UDSConfig


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
        self.logger = logging.getLogger("main").getChild("data_source")
        self.data_source_config: UDSConfig = None
        self.channel, self.channel_stream_method = None, None

    async def start(self):
        await self.connect_to_uds()
        self.prepare_stub()

    async def connect_to_uds(self):
        while True:
            try:
                self.config.reload()
                self.data_source_config = self.config.application_config.uds
                if not self.data_source_config.all_required:
                    raise ConfigurationException("Missing fields in Data Source configuration!")
                address_service = AddressService(self.config.application_config.consul)
                address = await address_service.get(self.data_source_config.address)

                await self.initiate_channel(address)
                break

            except (AttributeError, KeyError) as err:
                self.logger.error(f"Configuration argument missing: {err}")
                raise

            except ConsulServiceError:
                self.logger.error("No UDS address in consul. Waiting 1s and trying again")
                time.sleep(1)

            except grpc.RpcError:
                self.logger.error("No UDS response. Waiting 1s and trying again")
                time.sleep(1)

        self.logger.info(f"Connected to UDS on {address}")

    async def initiate_channel(self, address):
        self.logger.info(f"Connecting to UDS via {address}")
        self.channel = get_channel(address, asynchronous=True, ssl=False)
        if self.ssl:
            server_credentials = SSLCredentials(
                self.data_source_config.client_ca,
                self.data_source_config.client_private_key,
                self.data_source_config.client_public_key_chain,
            )
            self.channel = get_channel(
                address, asynchronous=True, ssl=True, ssl_credentials=server_credentials
            )

        self.logger.info(f"Data source channel on {address}")

    async def request(
        self, request: AgentExchangeRequest
    ) -> AsyncGenerator[Tuple[str, str, Any], None]:
        try:
            async for response in self._request(request):
                yield response
        except grpc.RpcError as err:
            await self.check_timeout_deadline_exceeded(err, request)

            self.logger.warning(f"{err!r} for {request} - trying to reconnect to UDS")
            reconnect_start_time = time.time()

            while True:
                if time.time() - reconnect_start_time < self.data_source_config.reconnect_timeout:
                    try:
                        await self.start()
                        break
                    except Exception:
                        self.logger.warning("Unable to reconnect to UDS - trying again")
                else:
                    self.logger.error("Unable to reconnect to UDS - reconnect timeout reached")
                    raise AgentDataSourceException()
            try:
                async for response in self._request(request):
                    yield response
            except grpc.RpcError as err:
                self.logger.warning(f"{err!r} for {request}")
                raise AgentDataSourceException()

    async def check_timeout_deadline_exceeded(self, err, request):
        try:
            if err.code == grpc.StatusCode.DEADLINE_EXCEEDED:
                self.logger.warning(f"{err!r} for {request}")
                raise AgentDataSourceException("Request timeout")
        except AttributeError:
            pass

    async def _request(self, request):
        async for response in self.channel_stream_method(
            self.prepare_request(request),
            timeout=self.data_source_config.timeout,
        ):
            # https://www.python.org/dev/peps/pep-0525/#asynchronous-yield-from
            for parsed in self.parse_response(response):
                yield parsed

    def prepare_request(self, request: AgentExchangeRequest) -> Any:
        raise NotImplementedError

    def parse_response(self, response: Any) -> Generator[Tuple[str, str, Any], None, None]:
        raise NotImplementedError

    def prepare_stub(self):
        """
        This method is to define stub and which it's method to use
        as self.channel_stream_method. It should look i.e.:

        stub = NameInputServiceStub(self.channel)
        self.channel_stream_method = stub.BatchGetMatchNameInputs
        """
        raise NotImplementedError

    async def stop(self) -> None:
        if self.channel:
            await self.channel.close()
            self.channel = None
