import argparse
import logging
import pathlib

from agent_base.agent import AgentRunner
from agent_base.grpc_service import GrpcService
from agent_base.utils import Config
from agent_base.utils.logger import get_logger

from bank_identification_codes import BankIdentificationCodesAgent
from bank_identification_codes.agent.agent_data_source import (
    BankIdentificationCodesAgentDataSource,
)
from bank_identification_codes.agent.agent_exchange import BankIdentificationCodesAgentExchange
from bank_identification_codes.agent.grpc_service import BankIdentificationCodesAgentGrpcServicer


def run(configuration_dirs, start_agent_exchange, start_grpc_service):
    config = Config(configuration_dirs=configuration_dirs, required=True)
    services = []

    if start_agent_exchange:
        services.append(
            BankIdentificationCodesAgentExchange(
                config,
                data_source=BankIdentificationCodesAgentDataSource(config),
            )
        )
    if start_grpc_service:
        services.append(
            GrpcService(config, agent_servicer=BankIdentificationCodesAgentGrpcServicer())
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
        "--agent-exchange",
        action="store_true",
        help="Start agent exchange",
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
        default="bank_id_codes.log",
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
        start_agent_exchange=args.agent_exchange,
        start_grpc_service=args.grpc,
    )


if __name__ == "__main__":
    main()
