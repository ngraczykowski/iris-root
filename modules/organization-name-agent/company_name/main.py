import argparse
import logging
import pathlib

import sentry_sdk
from agent_base.agent import AgentRunner
from agent_base.grpc_service import GrpcService
from agent_base.utils import Config
from agent_base.utils.logger import get_logger

from company_name.agent.agent import CompanyNameAgent
from company_name.agent.agent_config_grpc_servicer import CompanyNameAgentConfigServicer
from company_name.agent.agent_data_source import CompanyNameAgentDataSource
from company_name.agent.agent_exchange import CompanyNameAgentExchange
from company_name.agent.agent_grpc_servicer import CompanyNameAgentGrpcServicer


def run(
    configuration_dirs,
    start_agent_exchange,
    start_grpc_service,
    additional_knowledge_dir,
    use_ssl,
):
    config = Config(configuration_dirs=configuration_dirs, required=True)

    sentry_config = config.application_config.get("sentry", None)
    if sentry_config:
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
                config, data_source=CompanyNameAgentDataSource(config, ssl=use_ssl), ssl=use_ssl
            )
        )

    agent = CompanyNameAgent(config=config, additional_knowledge_dir=additional_knowledge_dir)

    if start_grpc_service:
        services.append(
            GrpcService(
                config,
                agent_servicer=CompanyNameAgentGrpcServicer(),
                servicers=(CompanyNameAgentConfigServicer(agent),),
                ssl=use_ssl,
            )
        )

    AgentRunner(config).run(
        agent=agent,
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
    parser.add_argument(
        "--log-file",
        type=str,
        default="org_name.log",
        help="Path to the file to save logs in",
    )
    parser.add_argument(
        "--ssl",
        action="store_true",
        help="Use ssl in grpc service and to connect with UDS & rabbitMQ",
    )
    args = parser.parse_args()
    # if not args.grpc and not args.agent_exchange:
    #     parser.error("No services to run")

    logger = get_logger(
        "main",
        log_level=logging.DEBUG if args.verbose else logging.INFO,
        log_file=args.log_file,
    )
    logger.info(f"Start logging to stdout and to {args.log_file} file")

    run(
        configuration_dirs=(args.configuration_dir,),
        start_grpc_service=args.grpc,
        start_agent_exchange=args.agent_exchange,
        additional_knowledge_dir=args.knowledge,
        use_ssl=args.ssl,
    )


if __name__ == "__main__":
    main()
