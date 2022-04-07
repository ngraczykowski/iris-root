import argparse
from concurrent import futures

import grpc
import omegaconf

import etl_pipeline.service.proto.api.etl_pipeline_pb2 as etl__pipeline__pb2
from etl_pipeline.config import service_config
from etl_pipeline.logger import get_logger
from etl_pipeline.service.servicer import EtlPipelineServiceServicer

logger = get_logger("main", "ms_pipeline.log")


def add_EtlPipelineServiceServicer_to_server(servicer, server):
    rpc_method_handlers = {
        "RunEtl": grpc.unary_unary_rpc_method_handler(
            servicer.RunEtl,
            request_deserializer=etl__pipeline__pb2.RunEtlRequest.FromString,
            response_serializer=etl__pipeline__pb2.RunEtlResponse.SerializeToString,
        ),
    }
    generic_handler = grpc.method_handlers_generic_handler(
        "silenteight.datascience.etlpipeline.v1.api.EtlPipelineService", rpc_method_handlers
    )
    server.add_generic_rpc_handlers((generic_handler,))


def serve(args):
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))

    add_EtlPipelineServiceServicer_to_server(EtlPipelineServiceServicer(args.ssl), server)
    if args.ssl:
        with open(service_config.GRPC_SERVER_TLS_PRIVATE_KEY, "rb") as f:
            private_key = f.read()
        with open(service_config.GRPC_SERVER_TLS_PUBLIC_KEY_CHAIN, "rb") as f:
            certificate_chain = f.read()
        with open(service_config.GRPC_SERVER_TLS_TRUSTED_CA, "rb") as f:
            cert_list = f.read()
        server_credentials = grpc.ssl_server_credentials(
            ((private_key, certificate_chain),), cert_list, require_client_auth=True
        )
        server.add_secure_port(
            f"{service_config.ETL_SERVICE_IP}:{service_config.ETL_SERVICE_PORT}",
            server_credentials,
        )
    else:
        try:
            address = service_config.ETL_SERVICE_ADDR
        except omegaconf.errors.ConfigAttributeError:
            address = f"{service_config.ETL_SERVICE_IP}:{service_config.ETL_SERVICE_PORT}"
        server.add_insecure_port(address)
    server.start()
    server.wait_for_termination()


if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("--ssl", action="store_true", required=False)
    args = parser.parse_args()
    serve(args)
