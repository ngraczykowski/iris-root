import argparse
import logging
import pathlib

from agent_base.agent import AgentRunner
from agent_base.utils import Config

from company_name.agent.agent import CompanyNameAgent
from company_name.agent.agent_data_source import CompanyNameAgentDataSource
from company_name.agent.agent_exchange import CompanyNameAgentExchange


def main():
    parser = argparse.ArgumentParser(description="Company name agent")
    parser.add_argument(
        "-c",
        "--configuration-dir",
        type=pathlib.Path,
        default=pathlib.Path("config"),
        help="Path for configuration files",
    )
    parser.add_argument(
        "-v",
        "--verbose",
        action="store_true",
        help="Increase verbosity for debug purpose",
    )
    args = parser.parse_args()

    logging.basicConfig(
        level=logging.DEBUG if args.verbose else logging.INFO,
        format="%(asctime)s %(name)-20s %(levelname)-8s %(message)s",
    )

    config = Config(configuration_dirs=(args.configuration_dir,), required=True)
    AgentRunner(config).run(
        CompanyNameAgent(config),
        services=[
            CompanyNameAgentExchange(
                config,
                data_source=CompanyNameAgentDataSource(config),
            )
        ],
    )


if __name__ == "__main__":
    main()
