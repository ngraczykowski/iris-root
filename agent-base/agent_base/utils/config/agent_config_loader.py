import os
from typing import Any, Dict, List

from agent_base.utils.config.agent_config import (
    AgentConfig,
    AgentServiceConfig,
    ConfigurationException,
    ConsulConfig,
    MessagingConfig,
    MessagingRequest,
    MessagingResponse,
    RabbitMQConfig,
    RMQSSLOptions,
    UDSConfig,
)


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
            port=self._get_param("consul_port", ["consul", "port"]),
        )

        messaging_request = MessagingRequest(
            exchange=self._get_param(
                "request-exchange", ["agent", "agent-exchange", "request", "exchange"]
            ),
            routing_key=self._get_param(
                "request-routing-key", ["agent", "agent-exchange", "request", "routing-key"]
            ),
            queue_name=self._get_param(
                "request-queue-name", ["agent", "agent-exchange", "request", "queue-name"]
            ),
            queue_durable=self._get_param(
                "request-queue-durable", ["agent", "agent-exchange", "request", "queue-durable"]
            ),
            queue_arguments=self._get_param(
                "request-queue-arguments", ["agent", "agent-exchange", "request", "queue-arguments"]
            ),
        )
        messaging_response = MessagingResponse(
            exchange=self._get_param(
                "response-exchange", ["agent", "agent-exchange", "response", "exchange"]
            ),
            routing_key=self._get_param(
                "response-routing-key", ["agent", "agent-exchange", "response", "routing-key"]
            ),
        )
        messaging_config = MessagingConfig(messaging_request, messaging_response)

        rabbitmq_ssl_options = RMQSSLOptions(
            cafile=self._get_param("rabbitmq_cafile", ["rabbitmq", "ssl_options", "cafile"]),
            keyfile=self._get_param("rabbitmq_keyfile", ["rabbitmq", "ssl_options", "keyfile"]),
            certfile=self._get_param("rabbitmq_cerfile", ["rabbitmq", "ssl_options", "certfile"]),
            verify=self._get_param("rabbitmq_verify", ["rabbitmq", "ssl_options", "verify"]),
        )
        rabbitmq_config = RabbitMQConfig(
            host=self._get_param("rabbitmq_host", ["rabbitmq", "host"]),
            port=self._get_param("rabbitmq_port", ["rabbitmq", "port"]),
            login=self._get_param("rabbitmq_login", ["rabbitmq", "login"]),
            password=self._get_param("rabbitmq_password", ["rabbitmq", "password"]),
            virtualhost=self._get_param("rabbitmq_virtualhost", ["rabbitmq", "virtualhost"]),
            ssl_options=rabbitmq_ssl_options,
        )

        uds_config = UDSConfig(
            address=self._get_param("uds_address", ["grpc", "client", "data-source", "address"]),
            timeout=self._get_param("uds_timeout", ["grpc", "client", "data-source", "timeout"]),
            client_ca=self._get_param(
                "uds_client_ca", ["grpc", "client", "data-source", "client_ca"]
            ),
            client_private_key=self._get_param(
                "uds_client_private_key", ["grpc", "client", "data-source", "client_private_key"]
            ),
            client_public_key_chain=self._get_param(
                "uds_client_public_key_chain",
                ["grpc", "client", "data-source", "client_public_key_chain"],
            ),
        )

        return AgentConfig(
            agent_service_config,
            consul_config,
            messaging_config,
            rabbitmq_config,
            uds_config,
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
                raise ConfigurationException
            elif not keys_from_yaml:
                return default

            _param = self._application_config.copy()
            for key in keys_from_yaml:
                try:
                    _param = _param[key]
                except (KeyError, TypeError):
                    if required:
                        raise ConfigurationException
                    else:
                        return default
            return _param
