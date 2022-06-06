import dataclasses
import os
from typing import Any, Dict, List, Optional


class AgentConfigError(Exception):
    pass


@dataclasses.dataclass
class AgentServiceConfig:
    processes: int
    grpc_port: int
    server_ca: Optional[str] = None
    server_private_key: Optional[str] = None
    server_public_key_chain: Optional[str] = None


@dataclasses.dataclass
class ConsulConfig:
    host: str
    port: int


@dataclasses.dataclass
class MessagingRequest:
    exchange: str
    routing_key: str
    queue_name: str
    queue_durable: bool = True
    queue_arguments: Optional[Dict[str, Any]] = None


@dataclasses.dataclass
class MessagingResponse:
    exchange: str
    routing_key: str


@dataclasses.dataclass
class MessagingConfig:
    request: MessagingRequest
    response: MessagingResponse


@dataclasses.dataclass
class SSLOptions:
    """
    verify param: 1 or 0 - to verify certificate or not
    """

    cafile: str
    keyfile: str
    certfile: str
    verify: int


@dataclasses.dataclass
class RabbitMQConfig:
    host: str
    port: int
    login: str
    password: str
    virtualhost: str
    ssl_options: Optional[SSLOptions] = None


@dataclasses.dataclass
class UDSConfig:
    address: str
    timeout: int
    client_ca: Optional[str]
    client_private_key: Optional[str]
    client_public_key_chain: Optional[str]


@dataclasses.dataclass
class AgentConfig:
    agent_grpc_service: AgentServiceConfig
    consul: ConsulConfig
    messaging: MessagingConfig = None
    rabbitmq: RabbitMQConfig = None
    uds: UDSConfig = None


class AgentConfigLoader:
    def __init__(self, application_config: Dict[str, Any]):
        self._application_config = application_config
        self._env_vars = application_config.get("environment-variables-mapping")

    def load(self) -> AgentConfig:
        agent_service_config = AgentServiceConfig(
            processes=int(self._get_param("processes", ["agent", "processes"], required=True)),
            grpc_port=int(self._get_param("grpc_port", ["agent", "grpc", "port"], required=True)),
            server_ca=self._get_param("grpc_server_ca", ["agent", "grpc", "server_ca"]),
            server_private_key=self._get_param(
                "grpc_server_private_key",
                ["agent", "grpc", "server_private_key"],
            ),
            server_public_key_chain=self._get_param(
                "grpc_server_public_key_chain",
                ["agent", "grpc", "server_public_key_chain"],
            ),
        )

        consul_config = ConsulConfig(
            host=self._get_param("consul_host", ["consul", "host"]),
            port=int(self._get_param("consul_port", ["consul", "port"])),
        )
        return AgentConfig(
            agent_service_config,
            consul_config,
        )

    def _get_param(
        self,
        env_variable: str,
        keys_from_yaml: List[str],
        default: Any = None,
        required: bool = False,
    ) -> Any:
        try:
            param = os.environ[str(self._env_vars.get(env_variable))]
            return param
        except (KeyError, TypeError):
            if not keys_from_yaml and required:
                raise AgentConfigError
            elif not keys_from_yaml:
                return default

            _param = self._application_config.copy()
            for key in keys_from_yaml:
                try:
                    _param = _param[key]
                except (KeyError, TypeError):
                    return default
            return _param
