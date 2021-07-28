import logging
import pathlib
from typing import Generator, Tuple

import pytest
import yaml
from silenteight.agents.v1.api.exchange.exchange_pb2 import (
    AgentExchangeRequest,
    AgentExchangeResponse,
)
from silenteight.datasource.api.name.v1.name_pb2 import NameFeatureInput

from company_name.agent.company_name_agent import CompanyNameAgent
from tests.agent.mocks.adjudication_engine_mock import AdjudicationEngineMock
from tests.agent.mocks.data_source_mock import DataSourceMock

# mark all test with asyncio
# - pytest will execute it as an asyncio task
pytestmark = pytest.mark.asyncio

logging.basicConfig(level=logging.INFO)

FEATURE_NAME = "features/companyName"


@pytest.fixture(autouse=True, scope="module")
def config():
    configuration_path = pathlib.Path("./config/application.yaml")
    configuration_path.symlink_to("application.local.yaml")
    with configuration_path.open("rt") as f:
        config = yaml.load(f, Loader=yaml.FullLoader)
    try:
        yield config
    finally:
        configuration_path.unlink()


@pytest.fixture(autouse=True)
async def ae_mock(config):
    async with AdjudicationEngineMock(config) as mock:
        yield mock


@pytest.fixture(autouse=True)
async def data_source_mock(config):
    async with DataSourceMock(
        address=config["grpc"]["client"]["data-source"]["address"]
    ) as mock:
        yield mock


@pytest.fixture(autouse=True)
async def company_name_agent():
    agent = CompanyNameAgent()
    await agent.start()
    try:
        yield agent
    finally:
        await agent.stop()


def get_solutions(
    response: AgentExchangeResponse,
) -> Generator[Tuple[str, str, str], None, None]:
    for agent_output in response.agent_outputs:
        for agent_feature_output in agent_output.features:
            yield (
                agent_output.match,
                agent_feature_output.feature,
                agent_feature_output.feature_solution.solution,
            )


async def test_single_request_returns_correct_identificators(
    ae_mock: AdjudicationEngineMock,
):
    for i in range(10):
        match, feature = f"match_{i}", FEATURE_NAME
        correlation_id = await ae_mock.send(
            AgentExchangeRequest(matches=[match], features=[feature])
        )
        response = await ae_mock.wait_for(correlation_id)

        assert response
        assert isinstance(response, AgentExchangeResponse)

        solutions = list(get_solutions(response))
        assert len(solutions) == 1
        assert solutions[0] == (match, feature, "NO_DATA")


async def test_multiple_matches(ae_mock: AdjudicationEngineMock):
    match_no = 10
    correlation_id = await ae_mock.send(
        AgentExchangeRequest(
            matches=[f"match_{i}" for i in range(match_no)], features=[FEATURE_NAME]
        )
    )
    response = await ae_mock.wait_for(correlation_id)

    assert response
    assert len(response.agent_outputs) == match_no
    assert {o.match for o in response.agent_outputs} == {
        f"match_{i}" for i in range(match_no)
    }
    assert all(
        len(agent_output.features) == 1 for agent_output in response.agent_outputs
    )


async def test_multiple_queries_with_different_times(
    ae_mock: AdjudicationEngineMock, data_source_mock: DataSourceMock
):
    data_source_mock.alerts = {"match_0": {"sleep": 2}, "match_1": {}}
    correlations = [
        await ae_mock.send(
            AgentExchangeRequest(matches=[f"match_{i}"], features=[FEATURE_NAME])
        )
        for i in (0, 1)
    ]

    for i in reversed((0, 1)):
        response = await ae_mock.wait_for(correlations[i])
        assert response.agent_outputs[0].match == f"match_{i}"


# just basic examples to check if every possible solution is possible
@pytest.mark.parametrize(
    ("data_source_info", "expected_solution"),
    (
        (
            {
                "alerted_party_type": NameFeatureInput.EntityType.INDIVIDUAL,
            },
            "AGENT_SKIPPED",
        ),
        ({}, "NO_DATA"),
        ({"alerted_party_names": [], "watchlist_names": ["google"]}, "NO_DATA"),
        (
            {"alerted_party_names": ["amazon", "facebook"], "watchlist_names": []},
            "NO_DATA",
        ),
        (
            {"alerted_party_names": ["google inc"], "watchlist_names": ["google"]},
            "MATCH",
        ),
        (
            {
                "alerted_party_names": ["amazon", "facebook"],
                "watchlist_names": ["amazon"],
            },
            "MATCH",
        ),
        (
            {"alerted_party_names": ["google inc"], "watchlist_names": ["amazon"]},
            "NO_MATCH",
        ),
    ),
)
async def test_solution(
    ae_mock: AdjudicationEngineMock,
    data_source_mock: DataSourceMock,
    data_source_info,
    expected_solution: str,
):
    data_source_mock.alerts = {"match": data_source_info}
    correlation_id = await ae_mock.send(
        AgentExchangeRequest(matches=["match"], features=[FEATURE_NAME])
    )
    # await asyncio.sleep(5)
    response = await ae_mock.wait_for(correlation_id)
    print("in test", correlation_id, response)
    solution = list(get_solutions(response))[0]
    assert solution == ("match", FEATURE_NAME, expected_solution)
