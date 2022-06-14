from typing import List

import grpc
import pytest
from grpc_health.v1.health_pb2 import HealthCheckRequest, HealthCheckResponse
from grpc_health.v1.health_pb2_grpc import HealthStub
from s8_python_network.grpc_channel import get_channel
from s8_python_network.ssl_credentials import SSLCredentials
from silenteight.agent.name.v1.api.name_agent_pb2 import (
    CompareNamesInput,
    CompareNamesRequest,
    GetNameAgentDescriptorRequest,
)
from silenteight.agent.name.v1.api.name_agent_pb2_grpc import NameAgentStub

from agent_base.agent import AgentRunner
from agent_base.example import JohnnyAgent, JohnnyAgentGrpcServicer
from agent_base.grpc_service import GrpcService
from agent_base.utils import Config

# mark all test with asyncio
# - pytest will execute it as an asyncio task
pytestmark = pytest.mark.asyncio


@pytest.fixture(autouse=True, scope="module")
def config():
    yield Config()


@pytest.fixture(autouse=True)
async def johnny_agent(config: Config):
    agent = JohnnyAgent(config)
    runner = AgentRunner(config)
    await runner.start(
        agent,
        services=[GrpcService(config, agent_servicer=JohnnyAgentGrpcServicer(), ssl=True)],
    )
    try:
        yield agent
    finally:
        await runner.stop()


@pytest.fixture()
async def channel(config: Config):
    return get_channel(
        f"localhost:{config.application_config.agent_grpc_service.grpc_port}", asynchronous=True
    )


@pytest.fixture()
async def secure_channel(config: Config):
    ssl_credentials = SSLCredentials(
        ca_filename="tests/resources/ssl_example/ca.pem",
        client_private_key_filename="tests/resources/ssl_example/client-key.pem",
        client_public_key_chain_filename="tests/resources/ssl_example/client.pem",
    )
    return get_channel(
        f"localhost:{config.application_config.agent_grpc_service.grpc_port}",
        asynchronous=True,
        ssl=True,
        ssl_credentials=ssl_credentials,
    )


@pytest.fixture()
async def stub(secure_channel: grpc.aio.Channel):
    return NameAgentStub(secure_channel)


async def test_health_check(secure_channel: grpc):
    stub = HealthStub(secure_channel)
    result = await stub.Check(HealthCheckRequest())
    assert result
    assert result.status == HealthCheckResponse.SERVING


async def test_descriptor(stub: NameAgentStub):
    result = await stub.GetNameAgentDescriptor(GetNameAgentDescriptorRequest())
    assert result
    assert result.name
    assert result.description
    assert set(result.value_names) == {"MATCH", "NO_MATCH", "NO_DATA"}


async def test_no_data_query(stub: NameAgentStub):
    async for result in stub.CompareNames(CompareNamesRequest(inputs=[CompareNamesInput()])):
        assert result
        assert result.result == "NO_DATA"
        assert result.input_index == 0


@pytest.mark.parametrize(
    ("names", "expected"),
    ((["Johnny Smith"], "MATCH"), (["John Smith"], "NO_MATCH"), ([], "NO_DATA")),
)
async def test_resolve_query(stub: NameAgentStub, names: List[str], expected: str):
    async for result in stub.CompareNames(
        CompareNamesRequest(inputs=[CompareNamesInput(alerted_names=names)])
    ):
        assert result
        assert result.result == expected
        assert result.input_index == 0
