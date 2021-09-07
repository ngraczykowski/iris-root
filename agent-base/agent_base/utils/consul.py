import logging
import random
from typing import Any, Mapping

import consul.aio


class ConsulService:
    def __init__(self, config):
        self.logger = logging.getLogger("consul_service")

        consul_config = config.get("consul")
        if consul_config:
            self.consul = consul.aio.Consul(**consul_config)
        else:
            self.consul = None
            self.logger.info("Consul not configured")

    async def get_service(self, key: str, **kwargs) -> str:
        service = await self._get_service_information(key, **kwargs)
        self.logger.debug(f"{key} : {service}")
        return f"{service['ServiceAddress'] or 'localhost'}:{service['ServicePort']}"

    async def _get_service_information(self, key: str, **kwargs) -> Mapping[str, Any]:
        if not self.consul:
            raise Exception("Consul not configured")

        _, services = await self.consul.catalog.service(key, **kwargs)
        if not services:
            raise Exception(f"Service {key} is not known")

        return random.choice(services)

    async def get(self, key: str):
        _, value = self.consul.kv.get(key)
        return value["Value"] if value else None
