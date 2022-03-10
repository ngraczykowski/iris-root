from concurrent import futures

import grpc
from silenteight.datasource.agentinput.api.v1 import (
    agent_input_service_pb2 as silenteight_dot_datasource_dot_agentinput_dot_api_dot_v1_dot_agent__input__service__pb2,
)


class AgentInputServiceServicer(object):
    """Missing associated documentation comment in .proto file."""

    def BatchCreateAgentInputs(self, request, context):
        """Missing associated documentation comment in .proto file."""
        print(request)
        context.set_code(grpc.StatusCode.OK)
        # context.set_details("Method not implemented!")
        raise request


def add_AgentInputServiceServicer_to_server(servicer, server):
    rpc_method_handlers = {
        "BatchCreateAgentInputs": grpc.unary_unary_rpc_method_handler(
            servicer.BatchCreateAgentInputs,
            request_deserializer=silenteight_dot_datasource_dot_agentinput_dot_api_dot_v1_dot_agent__input__service__pb2.BatchCreateAgentInputsRequest.FromString,
            response_serializer=silenteight_dot_datasource_dot_agentinput_dot_api_dot_v1_dot_agent__input__service__pb2.BatchCreateAgentInputsResponse.SerializeToString,
        ),
    }
    generic_handler = grpc.method_handlers_generic_handler(
        "silenteight.datasource.agentinput.api.v1.AgentInputService", rpc_method_handlers
    )
    server.add_generic_rpc_handlers((generic_handler,))


def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    add_AgentInputServiceServicer_to_server(AgentInputServiceServicer(), server)
    server.add_insecure_port("[::]:50052")
    server.start()
    server.wait_for_termination()


serve()
