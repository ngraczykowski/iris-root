from concurrent import futures

import grpc
from grpc_health.v1.health import SERVICE_NAME as health_service_name
from grpc_health.v1.health import HealthServicer
from grpc_health.v1.health_pb2_grpc import add_HealthServicer_to_server
from grpc_reflection.v1alpha import reflection

PORT = 9091
ADDRESS = f"localhost:{PORT}"


class AgentHealthServicer(HealthServicer):
    name = health_service_name

    def add_to_server(self, server):
        add_HealthServicer_to_server(self, server)


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
