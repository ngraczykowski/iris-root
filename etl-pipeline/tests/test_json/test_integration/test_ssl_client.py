import os
import subprocess
import time

import grpc

from etl_pipeline.config import ConsulServiceConfig
from etl_pipeline.service.proto.api.etl_pipeline_pb2_grpc import EtlPipelineServiceStub
from tests.test_json.test_integration.test_client import BaseGrpcTestCase


class TestSSLGrpcServer(BaseGrpcTestCase.TestGrpcServer):

    stub = None

    @classmethod
    def setUpClass(cls):
        cls.tearDownClass()
        environment = os.environ.copy()
        subprocess.Popen("tests/scripts/start_services_ssl.sh", env=environment)
        service_config = ConsulServiceConfig()
        with open(service_config.GRPC_CLIENT_TLS_CA, "rb") as f:
            ca = f.read()
        with open(service_config.GRPC_CLIENT_TLS_PRIVATE_KEY, "rb") as f:
            private_key = f.read()
        with open(service_config.GRPC_CLIENT_TLS_PUBLIC_KEY_CHAIN, "rb") as f:
            certificate_chain = f.read()
        server_credentials = grpc.ssl_channel_credentials(ca, private_key, certificate_chain)
        channel = grpc.secure_channel("localhost:9090", server_credentials)
        TestSSLGrpcServer.stub = EtlPipelineServiceStub(channel)
        time.sleep(BaseGrpcTestCase.TestGrpcServer.TIMEOUT)
