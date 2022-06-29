import random

from s8_python_network.consul import ConsulConfig, ConsulService


class AddressService:
    consul_prefix = "discovery:///"

    def __init__(self, config: ConsulConfig):
        self.consul = ConsulService(config)

    async def get(self, address: str) -> str:
        if address.startswith(self.consul_prefix):
            return await self.consul.get_service(address[len(self.consul_prefix) :])
        if "," in address:
            return random.choice(address.split(","))
        return address
