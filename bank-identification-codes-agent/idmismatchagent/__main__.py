import argparse
import pathlib
import time

from agent_base.agent import AgentRunner
from agent_base.grpc_service import GrpcService
from agent_base.utils import Config
from tstoolkit.utils import LogLevel, generate_logger, setup_logging

from idmismatchagent.agent import SearchCodeMismatchAgentInput, IdentificationMismatchAgent
from idmismatchagent.grpc_service import IdentificationMismatchAgentGrpcServicer


def run(configuration_dirs, start_grpc_service):
    config = Config(configuration_dirs=configuration_dirs, required=True)
    services = []

    if start_grpc_service:
        services.append(GrpcService(config, servicers=(IdentificationMismatchAgentGrpcServicer(),)))

    AgentRunner(config).run(
        IdentificationMismatchAgent(config=config),
        services=services,
    )


def main():
    setup_logging()
    logger = generate_logger(log_level=LogLevel.debug)
    logger.debug(
        IdentificationMismatchAgent().resolve(
            SearchCodeMismatchAgentInput(
                matching_field="WE REFER TO 23190617054158 FOR15,990.00",
                matching_text="190617",
                watchlist_search_codes=["190617"],
                watchlist_type="Individual",
                watchlist_bic_codes=[],
            )
        )
    )
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
        "-v",
        "--verbose",
        action="store_true",
        help="Increase verbosity for debug purpose",
    )
    args = parser.parse_args()

    run(
        configuration_dirs=(args.configuration_dir,),
        start_grpc_service=args.grpc,
    )


if __name__ == "__main__":
    main()
