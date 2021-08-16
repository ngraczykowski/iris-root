from typing import Callable

from grpc.aio import Server


class AgentGrpcServicer:
    name: str = None

    def __init__(self):
        self.create_resolve_task = None

    def set_create_resolve_task(self, create_resolve_task: Callable):
        self.create_resolve_task = create_resolve_task

    def add_to_server(self, server: Server) -> None:
        raise NotImplementedError()
