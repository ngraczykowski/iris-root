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

    async def get(self, key: str) -> str:
        service = await self.get_service(key)
        self.logger.debug(f"{key} : {service}")
        return f"{service['ServiceAddress'] or 'localhost'}:{service['ServicePort']}"

    async def get_service(self, key: str) -> Mapping[str, Any]:
        if not self.consul:
            raise Exception("Consul not configured")

        _, services = await self.consul.catalog.service(key)
        if not services:
            raise Exception(f"Service {key} is not known")

        return random.choice(services)


class AddressService:
    consul_prefix = "discovery:///"

    def __init__(self, config):
        self.consul = ConsulService(config)

    async def get(self, address: str) -> str:
        if address.startswith(self.consul_prefix):
            return await self.consul.get(address[len(self.consul_prefix) :])
        if "," in address:
            return random.choice(address.split(","))
        return address
