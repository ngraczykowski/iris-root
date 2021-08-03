import asyncio
import collections
import itertools
import logging
import math
import pathlib
import timeit

import yaml
from silenteight.agents.v1.api.exchange.exchange_pb2 import AgentExchangeRequest

from tests.agent.mocks.adjudication_engine_mock import AdjudicationEngineMock
from tests.agent.mocks.utils import (
    run_agent_in_process,
    run_data_source_mock_in_process,
)

NUMBER_OF_MATCHES = 100
NUMBER_OF_MATCHES_IN_MESSAGE = 1
NUMBER_OF_MESSAGES_IN_PROCESSING = 5
NUMBER_OF_NAMES_PER_SIDE_IN_MATCH = 1


class Matches:
    def __init__(self, number_of_matches):
        self.total_number_of_matches = number_of_matches
        self.in_processing = []
        self.to_process = collections.deque(self.all_matches())

    def all_matches(self):
        return (f"match_{i}" for i in range(self.total_number_of_matches))

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


def get_companies(filepath):
    with open(filepath, "rt") as f:
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


async def main(config, matches):
    await asyncio.sleep(10)

    async with AdjudicationEngineMock(config) as mock:
        start_time = last_time = timeit.default_timer()
        for _ in range(NUMBER_OF_MESSAGES_IN_PROCESSING):
            await mock.send(matches.produce_message())
            await asyncio.sleep(1)

        i = 0
        while matches.in_processing:
            if i and i % 100 == 0:
                current_time = timeit.default_timer()
                logging.info(
                    f"processed {i} messages ({current_time - last_time}s for last 100 messages)"
                )
                last_time = current_time

            _, received = await mock.get()
            i += 1
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

    matches = Matches(NUMBER_OF_MATCHES)
    random_alerts = dict(
        zip(
            matches.all_matches(),
            get_companies("tests/agent/sample_data/company_names.txt"),
        )
    )
    try:
        with run_data_source_mock_in_process(CONFIG, random_alerts):
            with run_agent_in_process():
                loop = asyncio.get_event_loop()
                loop.run_until_complete(main(CONFIG, matches))
    finally:
        configuration_path.unlink()
        print(matches.in_processing)
