from unittest.mock import patch

import aio_pika
import lz4.frame
import pytest
from google.protobuf.json_format import MessageToDict
from silenteight.agents.v1.api.exchange.exchange_pb2 import (
    AgentExchangeRequest,
    AgentExchangeResponse,
)

from agent_base.agent_exchange import AgentExchange
from agent_base.agent_exchange.agent_data_source import AgentDataSourceException
from tests.unit.mocks import MockAgent, MockAgentDataSource, MockPikaConnection

# mark all test with asyncio
# - pytest will execute it as an asyncio task
pytestmark = pytest.mark.asyncio


@pytest.fixture()
def agent(local_config):
    yield MockAgent(config=local_config)


@pytest.fixture()
def agent_exchange(local_config):
    with patch(
        "agent_base.agent_exchange.agent_exchange.PikaConnection",
        wraps=MockPikaConnection,
    ):
        yield AgentExchange(config=local_config, data_source=MockAgentDataSource())


def create_message(message: AgentExchangeRequest, routing_key="", timestamp=1, correlation_id=1):
    return aio_pika.Message(
        lz4.frame.compress(message.SerializeToString()),
        content_encoding="lz4",
        content_type="application/x-protobuf",
        delivery_mode=aio_pika.DeliveryMode.PERSISTENT,
        headers={
            "correlationId": correlation_id,
            "agentConfig": routing_key.replace(".", "/").replace("_", "."),
            "priority": 5,
        },
        priority=5,
        timestamp=timestamp,
        type="silenteight.agents.v1.api.exchange.AgentExchangeRequest",
    )


async def ask_for_many(agent_exchange, agent, matches, features, args):
    await agent_exchange.start(agent)

    for args_ids, args_values in args.items():
        agent_exchange.data_source.set(*args_ids, args_values)

    response = await agent_exchange.connections[0].callback(
        create_message(AgentExchangeRequest(matches=matches, features=features))
    )
    message = read_message(response)
    result = []

    await agent_exchange.stop()

    assert len(message.agent_outputs) == len(matches)
    assert {x.match for x in message.agent_outputs} == set(matches)

    for agent_output in message.agent_outputs:
        assert len(agent_output.features) == len(features)
        assert {x.feature for x in agent_output.features} == set(features)

        for feature in agent_output.features:
            result.append(
                (
                    agent_output.match,
                    feature.feature,
                    feature.feature_solution.solution,
                    MessageToDict(feature.feature_solution.reason),
                )
            )

    return result


async def ask(
    agent_exchange,
    agent,
    data_source_args,
    match_name="match_1",
    feature_name="features/anything",
):
    result = await ask_for_many(
        agent_exchange=agent_exchange,
        agent=agent,
        matches=[match_name],
        features=[feature_name],
        args={(match_name, feature_name): data_source_args},
    )
    _, _, solution, reason = result[0]
    return solution, reason


def read_message(response):
    return AgentExchangeResponse.FromString(lz4.frame.decompress(response.body))


async def test_agent_exchange(agent, agent_exchange):
    await agent_exchange.start(agent)
    await agent_exchange.stop()


async def test_agent_exchange_on_empty_request(agent, agent_exchange):
    result = await ask_for_many(
        agent_exchange=agent_exchange, agent=agent, matches=[], features=[], args={}
    )
    assert result == []


async def test_agent_exchange_on_agent_exception(agent, agent_exchange):
    def new_resolve(*args, **kwargs):
        raise Exception()

    agent.resolve = new_resolve
    solution = await ask(agent=agent, agent_exchange=agent_exchange, data_source_args=())

    assert solution == ("AGENT_ERROR", {})


async def test_agent_exchange_on_data_source_exception(agent, agent_exchange):
    async def new_request(*args, **kwargs):
        for x in ():
            yield x  # needed for this function to have aiter
        raise AgentDataSourceException()

    agent_exchange.data_source.request = new_request
    solution = await ask(agent=agent, agent_exchange=agent_exchange, data_source_args=())

    assert solution == ("DATA_SOURCE_ERROR", {})


async def test_agent_exchange_on_no_data_from_data_source(agent, agent_exchange):
    async def new_request(*args, **kwargs):
        for x in ():
            yield x  # needed for this function to have aiter

    agent_exchange.data_source.request = new_request
    solution = await ask(agent=agent, agent_exchange=agent_exchange, data_source_args=())

    assert solution == ("DATA_SOURCE_ERROR", {})


@pytest.mark.parametrize(
    "args",
    (
        ("arg1", "arg2"),
        ("arg",),
        (),
    ),
)
async def test_agent_exchange_use_data_source_result(agent, agent_exchange, args):
    solution = await ask(agent=agent, agent_exchange=agent_exchange, data_source_args=args)
    assert solution == ("RESOLVED", {"args": list(args)})


@pytest.mark.parametrize(
    "features",
    (
        ("features/names", "features/other_names", "features/even_different_names"),
        ("features/names", "features/other_names"),
        (),
    ),
)
async def test_agent_exchange_on_multiple_features(agent, agent_exchange, features):
    args = {("match_1", f): (f"{f}_arg1", f"{f}_arg2") for f in features}
    result = await ask_for_many(
        agent_exchange=agent_exchange,
        agent=agent,
        matches=["match_1"],
        features=features,
        args=args,
    )
    for _, feature, solution, reason in result:
        assert solution == "RESOLVED"
        assert reason == {"args": [f"{feature}_arg1", f"{feature}_arg2"]}


@pytest.mark.parametrize(
    "matches",
    ((), ("match_1",), ("match_1", "match_2", "match_3")),
)
async def test_agent_exchange_on_multiple_matches(agent, agent_exchange, matches):
    args = {(m, "features/some"): (f"{m}_arg1", f"{m}_arg2") for m in matches}
    result = await ask_for_many(
        agent_exchange=agent_exchange,
        agent=agent,
        matches=matches,
        features=["features/some"],
        args=args,
    )
    for match, _, solution, reason in result:
        assert solution == "RESOLVED"
        assert reason == {"args": [f"{match}_arg1", f"{match}_arg2"]}
