import asyncio
import logging

import grpc
from grpc_health.v1.health_pb2 import HealthCheckResponse
from grpc_reflection.v1alpha import reflection

from agent_base.agent import AgentService
from agent_base.grpc_service.health_servicer import AgentHealthServicer
from agent_base.grpc_service.servicer import AgentGrpcServicer
from agent_base.utils import Config


class GrpcService(AgentService):
    def __init__(
        self, config: Config, agent_servicer: AgentGrpcServicer, servicers=(), ssl: bool = False
    ):
        super().__init__(config=config)
        self.ssl = ssl
        self.logger = logging.getLogger("GrpcService")
        self.server = None
        self.agent_servicer = agent_servicer
        self.health_servicer = AgentHealthServicer(
            experimental_non_blocking=True, experimental_thread_pool=self.pool
        )
        self.servicers = (self.health_servicer, self.agent_servicer, *servicers)

    async def start(self, *args, **kwargs):
        self.logger.info("Starting grpc service")

        if self.server:
            raise Exception("Server already started")

        await super().start(*args, **kwargs)
        await self._start_server()
        self.agent_servicer.set_create_resolve_task(self.create_resolve_task)

        self.logger.info("Grpc service started")
        return self

    async def _start_server(self):
        self.server = grpc.aio.server()
        self._add_servicers()
        self._add_reflection()
        grpc_config = self.config.application_config["agent"]["grpc"]
        address = f"[::]:{grpc_config['port']}"

        if self.ssl:
            with open(grpc_config["server_ca"], "rb") as f:
                list_cert = f.read()
            with open(grpc_config["server_private_key"], "rb") as f:
                private_key = f.read()
            with open(grpc_config["server_public_key_chain"], "rb") as f:
                certificate_chain = f.read()
            server_credentials = grpc.ssl_server_credentials(
                ((private_key, certificate_chain),),
                root_certificates=list_cert,
                require_client_auth=True,
            )
            self.server.add_secure_port(address, server_credentials)
        else:
            self.server.add_insecure_port(address=address)

        await self.server.start()
        asyncio.get_event_loop().create_task(self.server.wait_for_termination())

    def _add_servicers(self):
        for servicer in self.servicers:
            servicer.add_to_server(self.server)
            if servicer.name:
                self.health_servicer.set(servicer.name, HealthCheckResponse.SERVING)

    def _add_reflection(self):
        service_names = (s.name for s in self.servicers)
        reflection.enable_server_reflection((reflection.SERVICE_NAME, *service_names), self.server)

    async def stop(self):
        self.logger.info("Stopping grpc service")

        if self.server:
            await self.server.stop(grace=1)
            self.server = None

        self.logger.info("Grpc service stopped")
