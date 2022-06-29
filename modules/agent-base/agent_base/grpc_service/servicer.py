from typing import Callable

from grpc.aio import Server


class GrpcServicer:
    name: str = None

    def add_to_server(self, server: Server) -> None:
        return


class AgentGrpcServicer(GrpcServicer):
    def __init__(self):
        self.create_resolve_task = None
        # this is set in a method below with
        # agent_base.agent.agent_service.AgentService's 'create_resolve_task' method

    def set_create_resolve_task(self, create_resolve_task: Callable):
        self.create_resolve_task = create_resolve_task
