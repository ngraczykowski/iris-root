import random
from typing import Mapping

from agent_base.utils.consul import ConsulService


class AddressService:
    consul_prefix = "discovery:///"

    def __init__(self, config: Mapping):
        self.consul = ConsulService(config)

    async def get(self, address: str) -> str:
        if address.startswith(self.consul_prefix):
            return await self.consul.get_service(address[len(self.consul_prefix) :])
        if "," in address:
            return random.choice(address.split(","))
        return address
