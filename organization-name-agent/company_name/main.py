import argparse
import logging
import pathlib

import sentry_sdk
from agent_base.agent import AgentRunner
from agent_base.grpc_service import GrpcService
from agent_base.utils import Config

from company_name.agent.agent import CompanyNameAgent
from company_name.agent.agent_data_source import CompanyNameAgentDataSource
from company_name.agent.agent_exchange import CompanyNameAgentExchange
from company_name.agent.grpc_service import CompanyNameAgentGrpcServicer


def run(
    configuration_dirs,
    start_agent_exchange,
    start_grpc_service,
    additional_knowledge_dir,
):
    config = Config(configuration_dirs=configuration_dirs, required=True)

    sentry_config = config.application_config["sentry"]
    if sentry_config["turn_on"]:
        sentry_sdk.init(
            traces_sample_rate=0.0,  # hard set to 0 to not increase usage cost
            release=f"organization-name-agent@{sentry_config['release']}",
            environment=sentry_config["environment"],
            sample_rate=sentry_config["sample_rate"],
        )

    services = []
    if start_agent_exchange:
        services.append(
            CompanyNameAgentExchange(
                config,
                data_source=CompanyNameAgentDataSource(config),
            )
        )
    if start_grpc_service:
        services.append(GrpcService(config, servicers=(CompanyNameAgentGrpcServicer(),)))

    AgentRunner(config).run(
        CompanyNameAgent(config=config, additional_knowledge_dir=additional_knowledge_dir),
        services=services,
    )


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
        "--grpc",
        action="store_true",
        help="Start grpc service",
    )
    parser.add_argument(
        "--agent-exchange",
        action="store_true",
        help="Start agent exchange",
    )
    parser.add_argument(
        "-k",
        "--knowledge",
        type=pathlib.Path,
        default=None,
        help="Path for knowledge files, like legal terms or countries",
    )
    parser.add_argument(
        "-v",
        "--verbose",
        action="store_true",
        help="Increase verbosity for debug purpose",
    )
    args = parser.parse_args()
    # if not args.grpc and not args.agent_exchange:
    #     parser.error("No services to run")

    logging.basicConfig(
        level=logging.DEBUG if args.verbose else logging.INFO,
        format="%(asctime)s %(name)-20s %(levelname)-8s %(message)s",
    )

    run(
        configuration_dirs=(args.configuration_dir,),
        start_grpc_service=args.grpc,
        start_agent_exchange=args.agent_exchange,
        additional_knowledge_dir=args.knowledge,
    )


if __name__ == "__main__":
    main()
