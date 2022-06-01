import argparse
import importlib
import json
import os
from concurrent import futures

import grpc
from google.protobuf.json_format import MessageToDict
from silenteight.datasource.agentinput.api.v1 import agent_input_service_pb2 as input__service__pb2
from silenteight.datasource.categories.api.v2 import category_service_pb2 as category_service_pb2
from silenteight.datasource.categories.api.v2 import category_value_service_pb2


class AgentInputServiceServicer(object):
    def BatchCreateAgentInputs(self, request, context):
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
        if context.code() == grpc.StatusCode.OK:
            os.makedirs("/tmp/", exist_ok=True)
            string_features = []
            with open(f"/tmp/features_{agent_input.match.replace('/','_')}.json", "w") as f:
                for agent_input in request.agent_inputs:
                    for feature in agent_input.feature_inputs:
                        path = feature.agent_feature_input.type_url.split("/")[1]
                        elements = path.split(".")
                        if "historicaldecisions" in elements:
                            module_name = elements[:-1] + ["historical_decisions" + "_pb2"]
                        elif "hittype" in elements:
                            module_name = elements[:-1] + ["hit_type" + "_pb2"]
                        else:
                            module_name = elements[:-1] + [elements[-3] + "_pb2"]

                        field_type = getattr(
                            importlib.import_module(".".join(module_name)), elements[-1]
                        )
                        new_field = field_type()
                        feature.agent_feature_input.Unpack(new_field)

                        string_features.append(MessageToDict(new_field))
                json.dump(string_features, f)

        return input__service__pb2.BatchCreateAgentInputsResponse(
            created_agent_inputs=created_agent_inputs
        )


class CategoryInputServiceServicer(object):
    def BatchCreateCategoryValues(self, request, context):
        parsed_categories = []
        context.set_code(grpc.StatusCode.OK)
        with open("tests/categories.txt", "r") as f:
            categories = [i.strip() for i in f.readlines()]
        with open("tests/categories1.txt", "w") as f:
            for category_request in request.requests:
                for category_value in category_request.category_values:
                    if "alerts" not in category_value.match:
                        context.set_code(grpc.StatusCode.INVALID_ARGUMENT)
                        break
                    if category_request.category not in categories:
                        context.set_code(grpc.StatusCode.INVALID_ARGUMENT)
                        break
                    f.write(str(category_request))
                    parsed_categories.append(
                        category_value_service_pb2.CreatedCategoryValue(
                            name=category_request.category, match=category_value.match
                        )
                    )

        return category_value_service_pb2.BatchCreateCategoryValuesResponse(
            created_category_values=parsed_categories
        )


class CategoryServiceServicer(object):
    """Missing associated documentation comment in .proto file."""

    def BatchCreateCategories(self, request, context):
        """Missing associated documentation comment in .proto file."""
        context.set_code(grpc.StatusCode.OK)

        with open("tests/categories.txt", "w") as f:
            for category in request.categories:
                f.write(str(category.name) + "\n")
        return category_service_pb2.BatchCreateCategoriesResponse(categories=request.categories)

    def ListCategories(self, request, context):
        """TODO(ahaczewski): Add RPCs for: removing single category and removing all categories."""
        context.set_code(grpc.StatusCode.UNIMPLEMENTED)
        context.set_details("Method not implemented!")
        raise NotImplementedError("Method not implemented!")


def add_CategoryServiceServicer_to_server(servicer, server):
    rpc_method_handlers = {
        "BatchCreateCategories": grpc.unary_unary_rpc_method_handler(
            servicer.BatchCreateCategories,
            request_deserializer=category_service_pb2.BatchCreateCategoriesRequest.FromString,
            response_serializer=category_service_pb2.BatchCreateCategoriesResponse.SerializeToString,
        ),
        "ListCategories": grpc.unary_unary_rpc_method_handler(
            servicer.ListCategories,
            request_deserializer=category_service_pb2.ListCategoriesRequest.FromString,
            response_serializer=category_service_pb2.ListCategoriesResponse.SerializeToString,
        ),
    }
    generic_handler = grpc.method_handlers_generic_handler(
        "silenteight.datasource.categories.api.v2.CategoryService", rpc_method_handlers
    )
    server.add_generic_rpc_handlers((generic_handler,))


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


def add_CategoryValueServiceServicer_to_server(servicer, server):
    rpc_method_handlers = {
        "BatchCreateCategoryValues": grpc.unary_unary_rpc_method_handler(
            servicer.BatchCreateCategoryValues,
            request_deserializer=category_value_service_pb2.BatchCreateCategoryValuesRequest.FromString,
            response_serializer=category_value_service_pb2.BatchCreateCategoryValuesResponse.SerializeToString,
        ),
    }
    generic_handler = grpc.method_handlers_generic_handler(
        "silenteight.datasource.categories.api.v2.CategoryValueService", rpc_method_handlers
    )
    server.add_generic_rpc_handlers((generic_handler,))


def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    add_CategoryValueServiceServicer_to_server(CategoryInputServiceServicer(), server)
    add_AgentInputServiceServicer_to_server(AgentInputServiceServicer(), server)
    add_CategoryServiceServicer_to_server(CategoryServiceServicer(), server)
    if args.ssl:
        with open("tests/ssl/ca.pem", "rb") as f:
            list_cert = f.read()
        with open("tests/ssl/uds-key.pem", "rb") as f:
            private_key = f.read()
        with open("tests/ssl/uds.pem", "rb") as f:
            certificate_chain = f.read()
        server_credentials = grpc.ssl_server_credentials(
            ((private_key, certificate_chain),),
            root_certificates=list_cert,
            require_client_auth=True,
        )
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
