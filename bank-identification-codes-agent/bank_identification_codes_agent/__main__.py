import argparse
import pathlib

from agent_base.agent import AgentRunner
from agent_base.grpc_service import GrpcService
from agent_base.utils import Config
from tstoolkit.utils import LogLevel, setup_logging

from bank_identification_codes_agent.agent import BankIdentificationCodesAgent
from bank_identification_codes_agent.grpc_service import BankIdentificationCodesAgentGrpcServicer


def run(configuration_dirs, start_grpc_service):
    config = Config(configuration_dirs=configuration_dirs, required=True)
    services = []

    if start_grpc_service:
        services.append(
            GrpcService(config, servicers=(BankIdentificationCodesAgentGrpcServicer(),))
        )

    AgentRunner(config).run(
        BankIdentificationCodesAgent(config=config),
        services=services,
    )


def main():
    parser = argparse.ArgumentParser(description="Bank identification code agent")
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
    args = parser.parse_args()

    setup_logging(log_level=LogLevel.debug if args.verbose else LogLevel.info)
    run(
        configuration_dirs=(args.configuration_dir,),
        start_grpc_service=args.grpc,
    )


if __name__ == "__main__":
    main()
