import argparse
import logging
import pathlib
import string

from silenteight.agents.v1.api.exchange.exchange_pb2 import AgentExchangeRequest
from silenteight.datasource.api.name.v1.name_pb2 import (
    BatchGetMatchNameInputsRequest,
    BatchGetMatchNameInputsResponse,
)
from silenteight.datasource.api.name.v1.name_pb2_grpc import NameInputServiceStub

from agent_base.agent import Agent, AgentRunner
from agent_base.agent_exchange import AgentDataSource, AgentExchange
from agent_base.utils import Config


class JohnnyAgentDataSource(AgentDataSource):
    async def start(self):
        await super().start()
        stub = NameInputServiceStub(self.channel)
        self.command = stub.BatchGetMatchNameInputs

    def prepare_request(
        self, request: AgentExchangeRequest
    ) -> BatchGetMatchNameInputsRequest:
        return BatchGetMatchNameInputsRequest(
            matches=request.matches, features=request.features
        )

    def parse_response(self, response: BatchGetMatchNameInputsResponse):
        for name_input in response.name_inputs:
            match = name_input.match
            for feature_input in name_input.name_feature_inputs:
                feature = feature_input.feature
                args = ([name.name for name in feature_input.alerted_party_names],)
                logging.debug(f"Response from data source {match}, {feature}, {args}")
                yield match, feature, args


class JohnnyAgent(Agent):
    def resolve(self, names):
        logging.debug(f"Agent resolve for {names}")

        if not names:
            return "NO_DATA", {}

        for name in names:
            if (
                "johnny"
                in name.lower()
                .translate(str.maketrans("", "", string.punctuation))
                .split()
            ):
                return "MATCH", {}

        return "NO_MATCH", {}


def main():
    parser = argparse.ArgumentParser(description="Sample agent")
    parser.add_argument(
        "-c",
        "--configuration-dir",
        type=pathlib.Path,
        default=pathlib.Path("config"),
        help="Path for configuration files",
    )
    parser.add_argument(
        "-v",
        "--verbose",
        action="store_true",
        help="Increase verbosity for debug purpose",
    )
    args = parser.parse_args()

    logging.basicConfig(
        level=logging.DEBUG if args.verbose else logging.INFO,
        format="%(asctime)s %(name)-20s %(levelname)-8s %(message)s",
    )

    config = Config(configuration_dirs=[args.configuration_dir])
    AgentRunner(config).run(
        JohnnyAgent(config),
        services=[AgentExchange(config, JohnnyAgentDataSource(config))],
    )


if __name__ == "__main__":
    main()
