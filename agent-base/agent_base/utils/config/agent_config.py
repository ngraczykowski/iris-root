import dataclasses
from typing import Any, Dict, Optional


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

    def __post_init__(self):
        if self.port:
            self.port = int(self.port)


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
class RMQSSLOptions:
    """
    verify param: 1 or 0 - to verify certificate or not
    """

    cafile: str
    keyfile: str
    certfile: str
    verify: int


@dataclasses.dataclass
class RabbitMQConfig:
    """
    Note: address, host & port are 'Optional', but at least one of: address, or host + port
    must be set to connect with Rabbit MQ
    """

    login: str
    password: str
    virtualhost: str
    host: Optional[str] = None
    port: Optional[str] = None
    addresses: Optional[str] = None
    ssl_options: Optional[RMQSSLOptions] = None


@dataclasses.dataclass
class UDSConfig:
    address: str
    timeout: int
    client_ca: Optional[str]
    client_private_key: Optional[str]
    client_public_key_chain: Optional[str]


@dataclasses.dataclass
class AgentConfig:
    agent_grpc_service: AgentServiceConfig = None
    consul: ConsulConfig = None
    messaging: MessagingConfig = None
    rabbitmq: RabbitMQConfig = None
    uds: UDSConfig = None


class ConfigurationException(Exception):
    pass
