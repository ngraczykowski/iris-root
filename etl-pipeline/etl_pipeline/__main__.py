# -*- coding: utf-8 -*-

from concurrent import futures

import grpc

import etl_pipeline.service.proto.api.etl_pipeline_pb2 as etl__pipeline__pb2
from etl_pipeline.config import service_config
from etl_pipeline.service.servicer import EtlPipelineServiceServicer


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


def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    add_EtlPipelineServiceServicer_to_server(EtlPipelineServiceServicer(), server)
    try:
        address = service_config.ETL_SERVICE_ADDR
    except:
        address = f"{service_config.ETL_SERVICE_HOSTNAME}:{service_config.ETL_SERVICE_PORT}"
    server.add_insecure_port(address)
    server.start()
    server.wait_for_termination()


if __name__ == "__main__":
    serve()
