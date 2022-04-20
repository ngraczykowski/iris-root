import argparse
import logging
import pathlib

import sentry_sdk
from agent_base.agent import AgentRunner
from agent_base.grpc_service import GrpcService
from agent_base.utils import Config

from hit_type.agent.agent import HitTypeAgent
from hit_type.agent.agent_data_source import HitTypeAgentDataSource
from hit_type.agent.agent_exchange import HitTypeAgentExchange
from hit_type.agent.agent_grpc_servicer import HitTypeAgentGrpcServicer


def run(configuration_dirs, start_agent_exchange, start_grpc_service, use_ssl):
    config = Config(configuration_dirs=configuration_dirs, required=True)

    sentry_config = config.application_config.get("sentry", None)

    if sentry_config:
        sentry_sdk.init(
            traces_sample_rate=0.0,  # hard set to 0 to not increase usage cost
            release=f"hit-type-agent@{sentry_config['release']}",
            environment=sentry_config["environment"],
            sample_rate=sentry_config["sample_rate"],
        )

    services = []
    if start_agent_exchange:
        services.append(
            HitTypeAgentExchange(
                config,
                data_source=HitTypeAgentDataSource(config, ssl=use_ssl),
                ssl=use_ssl,
            )
        )

    agent = HitTypeAgent(config=config)

    if start_grpc_service:
        services.append(GrpcService(config, agent_servicer=HitTypeAgentGrpcServicer(), ssl=use_ssl))

    AgentRunner(config).run(
        agent=agent,
        services=services,
    )


def main():
    parser = argparse.ArgumentParser(description="Hit Type agent")
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
        "--ssl",
        action="store_true",
        help="Use ssl in grpc service and to connect with UDS & rabbitMQ",
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
        use_ssl=args.ssl,
    )


if __name__ == "__main__":
    main()
