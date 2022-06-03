import argparse
import logging
import pathlib

import sentry_sdk
from agent_base.agent import AgentRunner
from agent_base.grpc_service import GrpcService
from agent_base.utils import Config
from agent_base.utils.logger import get_logger

from company_name_surrounding.agent import CompanyNameSurroundingAgent
from company_name_surrounding.grpc_service import CompanyNameSurroundingAgentGrpcServicer


def run(configuration_dirs, start_grpc_service):
    config = Config(configuration_dirs=configuration_dirs, required=True)

    sentry_config = config.application_config.get("sentry", None)
    if sentry_config:
        sentry_sdk.init(
            traces_sample_rate=0.0,  # hard set to 0 to not increase usage cost
            release=f"company-name-surrounding-agent@{sentry_config['release']}",
            environment=sentry_config["environment"],
            sample_rate=sentry_config["sample_rate"],
        )

    services = []

    if start_grpc_service:
        services.append(
            GrpcService(config, agent_servicer=CompanyNameSurroundingAgentGrpcServicer())
        )

    AgentRunner(config).run(CompanyNameSurroundingAgent(config=config), services=services)


def main():
    parser = argparse.ArgumentParser(description="Company name surrounding agent")
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
        "-v",
        "--verbose",
        action="store_true",
        help="Increase verbosity for debug purpose",
    )
    parser.add_argument(
        "--log-file",
        type=str,
        default="company_name_surrounding.log",
        help="Path to the file to save logs in",
    )
    args = parser.parse_args()
    logger = get_logger(
        "main",
        log_level=logging.DEBUG if args.verbose else logging.INFO,
        log_file=args.log_file,
    )
    logger.info(f"Start logging to stdout and to {args.log_file} file")
    run(
        configuration_dirs=(args.configuration_dir,),
        start_grpc_service=args.grpc,
    )


if __name__ == "__main__":
    main()
