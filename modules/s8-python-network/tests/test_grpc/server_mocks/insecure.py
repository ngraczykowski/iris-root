from concurrent import futures

import grpc
from grpc_reflection.v1alpha import reflection

from tests.test_grpc.server_mocks.health_service import AgentHealthServicer

PORT = 9091
ADDRESS = f"localhost:{PORT}"


def run_server():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    health_servicer = AgentHealthServicer(experimental_non_blocking=True)
    health_servicer.add_to_server(server)
    reflection.enable_server_reflection((reflection.SERVICE_NAME, health_servicer.name), server)
    server.add_insecure_port(address=ADDRESS)
    server.start()
    server.wait_for_termination()


if __name__ == "__main__":
    run_server()
