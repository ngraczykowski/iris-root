import logging

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

        self.services = {}

    async def get(self, key):
        if not self.consul:
            raise Exception("Consul not configured")

        if key not in self.services:
            await self._update_services()

        if key not in self.services:
            raise Exception(f"{key} is not known")

        self.logger.debug(f"{key} : {self.services[key]}")
        return f"{self.services[key]['Address'] or 'localhost'}:{self.services[key]['Port']}"

    async def _update_services(self):
        services = await self.consul.agent.services()
        self.logger.debug(f"{services}")
        self.services = {value["Service"]: value for value in services.values()}
        self.logger.debug(f"{self.services}")


class AddressService:
    consul_prefix = "discovery:///"

    def __init__(self, config):
        self.consul = ConsulService(config)

    async def get(self, address):
        if address.startswith(self.consul_prefix):
            return await self.consul.get(address[len(self.consul_prefix) :])
        return address
