from grpc_health.v1.health import SERVICE_NAME as health_service_name
from grpc_health.v1.health import HealthServicer
from grpc_health.v1.health_pb2_grpc import add_HealthServicer_to_server


class AgentHealthServicer(HealthServicer):
    name = health_service_name

    def add_to_server(self, server):
        add_HealthServicer_to_server(self, server)
