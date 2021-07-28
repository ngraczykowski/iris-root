import asyncio
import contextlib
import logging
import pathlib

import yaml

from company_name.base.agent_exchange import AgentExchange


class Agent:
    default_configuration_dir_path = pathlib.Path("./config")
    config_file_name = "application.yaml"

    def __init__(self, agent_exchange: AgentExchange = None, configuration_dir=None):
        self.agent_exchange = agent_exchange
        self.configuration_dir = (
            configuration_dir or self.default_configuration_dir_path
        )

        application_configuration = self.configuration_dir / self.config_file_name
        if application_configuration.exists():
            with application_configuration.open("rt") as config_file:
                self.config = yaml.load(config_file, Loader=yaml.FullLoader)
        else:
            self.config = {}

    async def start(self):
        if not self.config:
            raise Exception("Cannot start agent without configuration file")
        if self.agent_exchange:
            await self.agent_exchange.start()

    async def stop(self):
        if self.agent_exchange:
            await self.agent_exchange.stop()

    def run(self):
        loop = asyncio.get_event_loop()
        try:
            loop.run_until_complete(self.start())
            loop.run_forever()
        finally:
            logging.info(
                f"Finishing, {len(asyncio.all_tasks(loop))} running tasks will be cancelled"
            )
            tasks = asyncio.gather(*asyncio.all_tasks(loop))
            tasks.cancel()
            with contextlib.suppress(asyncio.CancelledError):
                loop.run_until_complete(tasks)
                loop.run_until_complete(loop.shutdown_asyncgens())
                loop.run_until_complete(self.stop())
        loop.close()

    def resolve(self, *args, **kwargs):
        raise NotImplementedError()
