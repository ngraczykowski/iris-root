import argparse
import logging
import pathlib
import string

from silenteight.agent.name.v1.api.name_agent_pb2 import (
    DESCRIPTOR,
    CompareNamesRequest,
    CompareNamesResponse,
    GetNameAgentDescriptorRequest,
    NameAgentDescriptor,
    NameAgentReason,
)
from silenteight.agent.name.v1.api.name_agent_pb2_grpc import (
    NameAgentServicer,
    add_NameAgentServicer_to_server,
)
from silenteight.agents.v1.api.exchange.exchange_pb2 import AgentExchangeRequest
from silenteight.datasource.api.name.v1.name_pb2 import (
    BatchGetMatchNameInputsRequest,
    BatchGetMatchNameInputsResponse,
)
from silenteight.datasource.api.name.v1.name_pb2_grpc import NameInputServiceStub

from agent_base.agent import Agent, AgentRunner
from agent_base.agent_exchange import AgentDataSource, AgentExchange
from agent_base.grpc_service import AgentGrpcServicer, GrpcService
from agent_base.utils import Config


# connecting to data source in bridge to request match data
# in this example using name data source
class JohnnyAgentDataSource(AgentDataSource):
    async def start(self):
        await super().start()
        stub = NameInputServiceStub(self.channel)
        self.channel_stream_method = stub.BatchGetMatchNameInputs

    def prepare_request(self, request: AgentExchangeRequest) -> BatchGetMatchNameInputsRequest:
        return BatchGetMatchNameInputsRequest(matches=request.matches, features=request.features)

    def parse_response(self, response: BatchGetMatchNameInputsResponse):
        for name_input in response.name_inputs:
            match = name_input.match
            for feature_input in name_input.name_feature_inputs:
                feature = feature_input.feature
                args = ([name.name for name in feature_input.alerted_party_names],)
                logging.debug(f"Response from data source {match}, {feature}, {args}")
                yield match, feature, args


# exposing agent methods by grpc
# below using existing service for name agent
class JohnnyAgentGrpcServicer(NameAgentServicer, AgentGrpcServicer):
    name = DESCRIPTOR.services_by_name["NameAgent"].full_name

    async def CompareNames(self, request: CompareNamesRequest, context) -> CompareNamesResponse:

        tasks = [
            (i, self.create_resolve_task(list(inputs.alerted_names)))
            for i, inputs in enumerate(request.inputs)
        ]
        for i, task in tasks:
            result, reason = await task
            yield CompareNamesResponse(input_index=i, result=result, reason=NameAgentReason())

    async def GetNameAgentDescriptor(
        self, request: GetNameAgentDescriptorRequest, context
    ) -> NameAgentDescriptor:
        return NameAgentDescriptor(
            name="Johnny name agent",
            description="Johnny name agent created as example dummy python agent",
            value_names=["MATCH", "NO_MATCH", "NO_DATA"],
        )

    def add_to_server(self, server):
        add_NameAgentServicer_to_server(self, server)


# main implementation of agent resolve method
class JohnnyAgent(Agent):
    def resolve(self, names):
        logging.debug(f"Agent resolve for {names}")

        if not names:
            return "NO_DATA", {}

        for name in names:
            if (
                "johnny"
                in name.lower().translate(str.maketrans("", "", string.punctuation)).split()
            ):
                return "MATCH", {}

        return "NO_MATCH", {}


def main():
    parser = argparse.ArgumentParser(description="Sample agent")
    parser.add_argument(
        "-c",
        "--configuration-dirs",
        type=pathlib.Path,
        default=(pathlib.Path("config"),),
        nargs="+",
        help="Path for configuration files",
    )
    parser.add_argument(
        "-v",
        "--verbose",
        action="store_true",
        help="Increase verbosity for debug purpose",
    )
    parser.add_argument(
        "--ssl",
        action="store_true",
    )
    args = parser.parse_args()

    logging.basicConfig(
        level=logging.DEBUG if args.verbose else logging.INFO,
        format="%(asctime)s %(name)-20s %(levelname)-8s %(message)s",
    )

    config = Config(configuration_dirs=args.configuration_dirs, required=True)
    AgentRunner(config).run(
        JohnnyAgent(config),
        services=[
            AgentExchange(config, JohnnyAgentDataSource(config, ssl=args.ssl), args.ssl),
            GrpcService(config, agent_servicer=JohnnyAgentGrpcServicer()),
        ],
    )


if __name__ == "__main__":
    main()
