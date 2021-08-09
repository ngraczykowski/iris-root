import asyncio
import functools

from agent_base.agent import Agent
from agent_base.utils import Config
from agent_base.utils.asyncio_utils import awaitify


class AgentService:
    def __init__(self, config: Config):
        self.config = config
        self.agent = None
        self.pool = None

    async def start(self, agent: Agent, pool=None):
        if not self.config.application_config:
            raise Exception("Cannot start agent without application configuration file")

        self.agent = agent
        self.pool = pool

    async def stop(self):
        self.agent = None
        self.pool = None

    def create_resolve_task(self, *args, **kwargs):
        func = functools.partial(self.agent.resolve, *args, **kwargs)
        if self.pool:
            return asyncio.get_running_loop().run_in_executor(self.pool, func)
        else:
            return asyncio.create_task(awaitify(func)())
