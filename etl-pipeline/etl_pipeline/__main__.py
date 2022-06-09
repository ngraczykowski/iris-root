# flake8: noqa: E402


import multiprocessing

try:
    multiprocessing.set_start_method("spawn")
except RuntimeError:
    pass
import argparse
import asyncio
import contextlib
import functools
from concurrent import futures

import grpc
import omegaconf

import etl_pipeline.service.proto.api.etl_pipeline_pb2 as etl__pipeline__pb2
from etl_pipeline.config import ConsulServiceConfig, ConsulServiceError
from etl_pipeline.learning.proto.etl_learning_pb2_grpc import (
    add_EtlLearningServiceServicer_to_server,
)
from etl_pipeline.learning.service import EtlLearningServiceServicer
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


async def serve(args):
    service_config = ConsulServiceConfig()
    try:
        max_length = service_config.MAX_MESSAGE_LENGTH
    except (KeyError, ConsulServiceError):
        max_length = 27838500
    server = grpc.aio.server(
        futures.ThreadPoolExecutor(max_workers=10),
        options=[
            ("grpc.max_send_message_length", max_length),
            ("grpc.max_receive_message_length", max_length),
        ],
    )
    add_EtlLearningServiceServicer_to_server(EtlLearningServiceServicer(args.ssl), server)

    add_EtlPipelineServiceServicer_to_server(EtlPipelineServiceServicer(args.ssl), server)
    if args.ssl:
        with open(service_config.grpc_server_tls_private_key, "rb") as f:
            private_key = f.read()
        with open(service_config.grpc_server_tls_public_key_chain, "rb") as f:
            certificate_chain = f.read()
        with open(service_config.grpc_server_tls_trusted_ca, "rb") as f:
            cert_list = f.read()
        server_credentials = grpc.ssl_server_credentials(
            ((private_key, certificate_chain),), cert_list, require_client_auth=True
        )
        server.add_secure_port(
            f"{service_config.etl_service_ip}:{service_config.etl_service_port}",
            server_credentials,
        )
    else:
        try:
            address = service_config.ETL_SERVICE_ADDR
        except (omegaconf.errors.ConfigAttributeError, KeyError, ConsulServiceError):
            address = f"{service_config.etl_service_ip}:{service_config.etl_service_port}"
        server.add_insecure_port(address)
    await server.start()
    asyncio.get_event_loop().create_task(server.wait_for_termination())


def run(start_callback, end_callback):
    loop = asyncio.get_event_loop()
    try:
        loop.run_until_complete(start_callback())
        loop.run_forever()
    finally:
        tasks = asyncio.gather(*asyncio.Task.all_tasks(loop))
        tasks.cancel()
        with contextlib.suppress(asyncio.CancelledError):
            loop.run_until_complete(tasks)
            loop.run_until_complete(loop.shutdown_asyncgens())
            loop.run_until_complete(end_callback())
    loop.close()


async def stop():
    pass


if __name__ == "__main__":

    parser = argparse.ArgumentParser()
    parser.add_argument("--ssl", action="store_true", required=False)
    args = parser.parse_args()
    run(functools.partial(serve, args), stop)
