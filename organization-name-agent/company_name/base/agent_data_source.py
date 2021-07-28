import logging
from typing import AsyncGenerator

import grpc
from silenteight.datasource.api.name.v1.name_pb2 import (
    BatchGetMatchNameInputsRequest,
    BatchGetMatchNameInputsResponse,
)
from silenteight.datasource.api.name.v1.name_pb2_grpc import NameInputServiceStub

from company_name.base.address_service import AddressService
from company_name.base.exception import AgentException


class AgentDataSourceException(AgentException):
    retry: True


class AgentDataSource:
    def __init__(self, config):
        self.config = config
        self.address_service = AddressService(config)
        self.channel, self.stub = None, None

    async def start(self):
        address = await self.address_service.get(
            self.config["grpc"]["client"]["data-source"]["address"]
        )
        self.channel = grpc.aio.insecure_channel(address)
        self.stub = NameInputServiceStub(self.channel)

    async def request(
        self, request: BatchGetMatchNameInputsRequest
    ) -> AsyncGenerator[BatchGetMatchNameInputsResponse, None]:
        try:
            async for response in self.stub.BatchGetMatchNameInputs(
                request,
                timeout=self.config["grpc"]["client"]["data-source"].get("timeout"),
            ):
                # https://www.python.org/dev/peps/pep-0525/#asynchronous-yield-from
                yield response
        except grpc.RpcError as err:
            logging.warning(f"{err!r} for {request}")
            raise AgentDataSourceException()

    async def stop(self) -> None:
        if self.channel:
            await self.channel.close()
