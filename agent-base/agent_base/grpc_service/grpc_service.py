import asyncio
import logging

import grpc
from grpc_health.v1.health_pb2 import HealthCheckResponse
from grpc_reflection.v1alpha import reflection

from agent_base.agent import AgentService
from agent_base.grpc_service.health_servicer import AgentHealthServicer
from agent_base.utils import Config


class GrpcService(AgentService):
    def __init__(self, config: Config, servicers=()):
        super().__init__(config=config)
        self.logger = logging.getLogger("GrpcService")
        self.server = None
        self.health_servicer = AgentHealthServicer(
            experimental_non_blocking=True, experimental_thread_pool=self.pool
        )
        self.servicers = (self.health_servicer, *servicers)

    async def start(self, *args, **kwargs):
        self.logger.debug("Starting grpc service")

        if self.server:
            raise Exception("Server already started")

        await super().start(*args, **kwargs)
        await self._start_server()
        for servicer in self.servicers:
            servicer.set_create_resolve_task(self.create_resolve_task)

        self.logger.debug("Grpc service started")
        return self

    async def _start_server(self):
        self.server = grpc.aio.server()
        self._add_servicers()
        self._add_reflection()
        self.server.add_insecure_port(
            address=f"[::]:{self.config.application_config['agent']['grpc']['port']}"
        )
        await self.server.start()
        asyncio.get_event_loop().create_task(self.server.wait_for_termination())

    def _add_servicers(self):
        for servicer in self.servicers:
            servicer.add_to_server(self.server)
            if servicer.name:
                self.health_servicer.set(servicer.name, HealthCheckResponse.SERVING)

    def _add_reflection(self):
        service_names = (s.name for s in self.servicers)
        reflection.enable_server_reflection(
            (reflection.SERVICE_NAME, *service_names), self.server
        )

    async def stop(self):
        self.logger.debug("Stopping grpc service")

        if self.server:
            await self.server.stop(grace=1)
            self.server = None

        self.logger.debug("Grpc service stopped")
