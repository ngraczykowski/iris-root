import asyncio
from concurrent import futures

import grpc
from grpc_reflection.v1alpha import reflection

from s8_python_network.utils import run_async
from tests.test_grpc.server_mocks.health_service import AgentHealthServicer

PORT = 9093
ADDRESS = f"localhost:{PORT}"


async def run_server():
    server = grpc.aio.server(futures.ThreadPoolExecutor(max_workers=10))
    health_servicer = AgentHealthServicer(experimental_non_blocking=True)
    health_servicer.add_to_server(server)
    reflection.enable_server_reflection((reflection.SERVICE_NAME, health_servicer.name), server)
    server.add_insecure_port(address=ADDRESS)
    await server.start()
    asyncio.get_event_loop().create_task(server.wait_for_termination())


async def stop():
    pass


if __name__ == "__main__":
    run_async(run_server, stop)
