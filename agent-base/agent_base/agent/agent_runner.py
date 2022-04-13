import asyncio
import concurrent.futures
import contextlib
import functools
import logging
import sys
from typing import List

from agent_base.agent.agent import Agent
from agent_base.agent.agent_service import AgentService
from agent_base.utils import Config


def run(start_callback, end_callback):
    loop = asyncio.get_event_loop()
    try:
        loop.run_until_complete(start_callback())
        loop.run_forever()
    finally:
        logging.info(f"Finishing, {len(asyncio.all_tasks(loop))} running tasks will be cancelled")
        tasks = asyncio.gather(*asyncio.all_tasks(loop))
        tasks.cancel()
        with contextlib.suppress(asyncio.CancelledError):
            loop.run_until_complete(tasks)
            loop.run_until_complete(loop.shutdown_asyncgens())
            loop.run_until_complete(end_callback())
    loop.close()


class AgentRunner:
    def __init__(self, config: Config):
        self.application_config = config.application_config
        self.logger = logging.getLogger("runner")
        self.logger.addHandler(logging.StreamHandler(sys.stdout))
        self.pool = None
        self.running = []

    async def start(self, agent: Agent, services: List[AgentService]):
        if self.running:
            raise Exception("Already running, stop services first")

        processes = self.application_config["agent"].get("processes")
        if not self.pool and processes and processes > 1:
            self.pool = concurrent.futures.ProcessPoolExecutor(processes)

        self.running = services
        await asyncio.gather(*(service.start(agent, self.pool) for service in services))

    async def stop(self):
        await asyncio.gather(*(service.stop() for service in self.running))
        self.running = []

        if self.pool:
            self.pool.shutdown()

    def run(self, agent: Agent, services: List[AgentService]):
        run(functools.partial(self.start, agent, services), self.stop)
