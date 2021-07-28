import asyncio
import collections
import contextlib
import itertools
import logging
import math
import multiprocessing
import pathlib
import sys
import timeit

import yaml
from silenteight.agents.v1.api.exchange.exchange_pb2 import AgentExchangeRequest

from company_name.main import main as agent_run
from tests.agent.mocks.adjudication_engine_mock import AdjudicationEngineMock
from tests.agent.mocks.data_source_mock import DataSourceMock

NUMBER_OF_MATCHES = 10000
NUMBER_OF_MATCHES_IN_MESSAGE = 10
NUMBER_OF_MESSAGES_IN_PROCESSING = 40
NUMBER_OF_NAMES_PER_SIDE_IN_MATCH = 1


class Matches:
    def __init__(self):
        self.in_processing = []
        self.to_process = collections.deque(self.all_matches())

    @staticmethod
    def all_matches():
        return (f"match_{i}" for i in range(NUMBER_OF_MATCHES))

    def produce_message(self):
        matches = [
            self.to_process.popleft()
            for _ in range(min(len(self.to_process), NUMBER_OF_MATCHES_IN_MESSAGE))
        ]
        if not matches:
            return None
        self.in_processing.extend(matches)
        return AgentExchangeRequest(matches=matches, features=["features/companyName"])

    def accept_received(self, received):
        for o in received.agent_outputs:
            try:
                self.in_processing.remove(o.match)
            except ValueError:
                pass

            if o.features[0].feature_solution.solution not in (
                "NO_MATCH",
                "INCONCLUSIVE",
                "MATCH",
            ):
                # cause NO_DATA or AGENT_SKIPPED are probably faster
                raise Exception(
                    "Incorrect solution, check data source mock initialization"
                )


@contextlib.contextmanager
def _run_in_process(f):
    p = multiprocessing.Process(target=f, args=())
    p.start()
    try:
        yield
    except Exception as err:
        print(repr(err))
        raise
    finally:
        p.kill()
        p.join()


@contextlib.contextmanager
def run_agent():
    def f():
        try:
            agent_run()
        finally:
            sys.stdout.flush()
            sys.stderr.flush()

    with _run_in_process(f):
        yield


def get_companies():
    with open("tests/agent/sample_data/company_names.txt") as f:
        companies = [line.strip() for line in f]
        for _, c1, c2 in zip(
            range(NUMBER_OF_MATCHES),
            itertools.cycle(
                itertools.permutations(companies, NUMBER_OF_NAMES_PER_SIDE_IN_MATCH)
            ),
            itertools.cycle(
                itertools.permutations(
                    reversed(companies), NUMBER_OF_NAMES_PER_SIDE_IN_MATCH
                )
            ),
        ):
            yield {"alerted_party_names": c1, "watchlist_names": c2}


@contextlib.contextmanager
def run_data_source_mock():
    def f():
        loop = asyncio.get_event_loop()
        mock = DataSourceMock(CONFIG["grpc"]["client"]["data-source"]["address"])
        mock.alerts = dict(
            zip((f"match_{i}" for i in range(NUMBER_OF_MATCHES)), get_companies())
        )
        loop.create_task(mock.start())
        try:
            loop.run_forever()
        finally:
            sys.stdout.flush()
            loop.run_until_complete(mock.stop())

    with _run_in_process(f):
        yield


async def main(config):
    await asyncio.sleep(10)
    matches = Matches()

    async with AdjudicationEngineMock(config) as mock:
        start_time = last_time = timeit.default_timer()
        for _ in range(NUMBER_OF_MESSAGES_IN_PROCESSING):
            await mock.send(matches.produce_message())

        for i in range(math.floor(NUMBER_OF_MATCHES / NUMBER_OF_MATCHES_IN_MESSAGE)):
            if i and i % 100 == 0:
                current_time = timeit.default_timer()
                logging.info(
                    f"processed {i} messages ({current_time - last_time}s for last 100 messages)"
                )
                last_time = current_time

            _, received = await mock.get()
            matches.accept_received(received)

            message = matches.produce_message()
            if message:
                await mock.send(message)
        print(
            f"{NUMBER_OF_MATCHES} matches"
            f" in {math.ceil(NUMBER_OF_MATCHES / NUMBER_OF_MATCHES_IN_MESSAGE)} messages"
            f" done in {timeit.default_timer() - start_time} seconds"
        )


if __name__ == "__main__":
    logging.basicConfig(
        level=logging.DEBUG,
        format="%(asctime)s %(name)-20s %(levelname)-8s %(message)s",
    )

    configuration_path = pathlib.Path("./config/application.yaml")
    configuration_path.symlink_to("application.local.yaml")
    with configuration_path.open("rt") as f:
        CONFIG = yaml.load(f, Loader=yaml.FullLoader)

    try:
        with run_data_source_mock():
            with run_agent():
                loop = asyncio.get_event_loop()
                loop.run_until_complete(main(CONFIG))
    finally:
        configuration_path.unlink()
