from grpc_health.v1.health import SERVICE_NAME as health_service_name
from grpc_health.v1.health import HealthServicer
from grpc_health.v1.health_pb2_grpc import add_HealthServicer_to_server

from agent_base.grpc_service.servicer import AgentGrpcServicer


class AgentHealthServicer(HealthServicer, AgentGrpcServicer):
    name = health_service_name

    def add_to_server(self, server):
        add_HealthServicer_to_server(self, server)
