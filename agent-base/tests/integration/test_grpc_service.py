import pathlib
from typing import List

import grpc
import pytest
from grpc_health.v1.health_pb2 import HealthCheckRequest, HealthCheckResponse
from grpc_health.v1.health_pb2_grpc import HealthStub
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
    configuration_path = pathlib.Path("./config/application.yaml")
    configuration_path.symlink_to("application.local.yaml")
    try:
        yield Config([configuration_path.parent])
    finally:
        configuration_path.unlink()


@pytest.fixture(autouse=True)
async def johnny_agent(config: Config):
    agent = JohnnyAgent(config)
    runner = AgentRunner(config)
    await runner.start(
        agent,
        services=[GrpcService(config, agent_servicer=JohnnyAgentGrpcServicer())],
    )
    try:
        yield agent
    finally:
        await runner.stop()


@pytest.fixture()
async def channel(config: Config):
    return grpc.aio.insecure_channel(
        f"localhost:{config.application_config['agent']['grpc']['port']}"
    )


@pytest.fixture()
async def stub(channel: grpc.aio.Channel):
    return NameAgentStub(channel)


async def test_health_check(channel: grpc):
    stub = HealthStub(channel)
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
