import asyncio
from concurrent import futures

import grpc
from grpc_reflection.v1alpha import reflection

from s8_python_network.utils import run_async
from tests.test_grpc.server_mocks.health_service import AgentHealthServicer

PORT = 9094
ADDRESS = f"localhost:{PORT}"

ssl_files_dir = "tests/test_grpc/ssl_example"


async def run_server():
    server = grpc.aio.server(futures.ThreadPoolExecutor(max_workers=10))
    health_servicer = AgentHealthServicer(experimental_non_blocking=True)
    health_servicer.add_to_server(server)
    reflection.enable_server_reflection((reflection.SERVICE_NAME, health_servicer.name), server)

    with open(f"{ssl_files_dir}/ca.pem", "rb") as f:
        list_cert = f.read()
    with open(f"{ssl_files_dir}/server-key.pem", "rb") as f:
        private_key = f.read()
    with open(f"{ssl_files_dir}/server.pem", "rb") as f:
        certificate_chain = f.read()
    server_credentials = grpc.ssl_server_credentials(
        ((private_key, certificate_chain),),
        root_certificates=list_cert,
        require_client_auth=True,
    )
    server.add_secure_port(address=ADDRESS, server_credentials=server_credentials)
    await server.start()
    asyncio.get_event_loop().create_task(server.wait_for_termination())


async def stop():
    pass


if __name__ == "__main__":
    run_async(run_server, stop)
