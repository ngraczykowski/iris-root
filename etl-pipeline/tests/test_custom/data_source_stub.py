import argparse
from concurrent import futures

import grpc
from silenteight.datasource.agentinput.api.v1 import agent_input_service_pb2 as input__service__pb2


class AgentInputServiceServicer(object):
    """Missing associated documentation comment in .proto file."""

    def BatchCreateAgentInputs(self, request, context):
        """Missing associated documentation comment in .proto file."""
        created_agent_inputs = []
        context.set_code(grpc.StatusCode.OK)
        for agent_input in request.agent_inputs:

            if "alerts" not in agent_input.match:

                context.set_code(grpc.StatusCode.INVALID_ARGUMENT)
                break
            created_agent_inputs.append(
                input__service__pb2.CreatedAgentInput(
                    name=agent_input.name, match=agent_input.match
                )
            )

        return input__service__pb2.BatchCreateAgentInputsResponse(
            created_agent_inputs=created_agent_inputs
        )


def add_AgentInputServiceServicer_to_server(servicer, server):
    rpc_method_handlers = {
        "BatchCreateAgentInputs": grpc.unary_unary_rpc_method_handler(
            servicer.BatchCreateAgentInputs,
            request_deserializer=input__service__pb2.BatchCreateAgentInputsRequest.FromString,
            response_serializer=input__service__pb2.BatchCreateAgentInputsResponse.SerializeToString,
        ),
    }
    generic_handler = grpc.method_handlers_generic_handler(
        "silenteight.datasource.agentinput.api.v1.AgentInputService", rpc_method_handlers
    )
    server.add_generic_rpc_handlers((generic_handler,))


def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    add_AgentInputServiceServicer_to_server(AgentInputServiceServicer(), server)
    if args.ssl:
        with open("tests/ssl/server-key.pem", "rb") as f:
            private_key = f.read()
        with open("tests/ssl/server.pem", "rb") as f:
            certificate_chain = f.read()
        server_credentials = grpc.ssl_server_credentials(((private_key, certificate_chain),))
        server.add_secure_port("localhost:50052", server_credentials)
    else:
        server.add_insecure_port("localhost:50052")
    server.start()
    server.wait_for_termination()


if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("--ssl", action="store_true", required=False)
    args = parser.parse_args()
    serve()
