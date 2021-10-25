import argparse
import logging
import pathlib

from agent_base.agent import AgentRunner
from agent_base.grpc_service import GrpcService
from agent_base.utils import Config

from company_name_surrounding.agent import CompanyNameSurroundingAgent
from company_name_surrounding.grpc_service import CompanyNameSurroundingAgentGrpcServicer


def run(configuration_dirs, start_grpc_service):
    config = Config(configuration_dirs=configuration_dirs, required=True)
    services = []

    if start_grpc_service:
        services.append(
            GrpcService(config, servicers=(CompanyNameSurroundingAgentGrpcServicer(),))
        )

    AgentRunner(config).run(CompanyNameSurroundingAgent(config=config), services=services)


def main():
    parser = argparse.ArgumentParser(description="Strict name agent")
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
    args = parser.parse_args()
    logging.basicConfig(
        level=logging.DEBUG,
        format="%(asctime)s %(name)-20s %(levelname)-8s %(message)s",
    )
    run(
        configuration_dirs=(args.configuration_dir,),
        start_grpc_service=args.grpc,
    )


if __name__ == "__main__":
    main()
