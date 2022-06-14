import dataclasses
from typing import Any, Dict, Optional

from s8_python_network.consul import ConsulConfig
from s8_python_network.pika_connection import RabbitMQConfig


@dataclasses.dataclass
class AgentServiceConfig:
    """
    processes: int
    grpc_port: int
    server_ca: Optional[str] = None
    server_private_key: Optional[str] = None
    server_public_key_chain: Optional[str] = None
    """

    processes: int
    grpc_port: int
    server_ca: Optional[str] = None
    server_private_key: Optional[str] = None
    server_public_key_chain: Optional[str] = None

    def __post_init__(self):
        if self.grpc_port:
            self.grpc_port = int(self.grpc_port)
        if self.processes:
            self.processes = int(self.processes)


@dataclasses.dataclass
class MessagingRequest:
    """
    exchange: str
    routing_key: str
    queue_name: str
    queue_durable: bool = True
    queue_arguments: Optional[Dict[str, Any]] = None
    """

    exchange: str
    routing_key: str
    queue_name: str
    queue_durable: bool = True
    queue_arguments: Optional[Dict[str, Any]] = None


@dataclasses.dataclass
class MessagingResponse:
    """
    exchange: str
    routing_key: str
    """

    exchange: str
    routing_key: str


@dataclasses.dataclass
class MessagingConfig:
    """
    request: MessagingRequest
    response: MessagingResponse
    """

    request: MessagingRequest
    response: MessagingResponse


@dataclasses.dataclass
class UDSConfig:
    """
    address: str
    timeout: int
    client_ca: Optional[str]
    client_private_key: Optional[str]
    client_public_key_chain: Optional[str]
    """

    address: str
    timeout: int
    client_ca: Optional[str]
    client_private_key: Optional[str]
    client_public_key_chain: Optional[str]

    def __post_init__(self):
        if self.timeout:
            self.timeout = int(self.timeout)


@dataclasses.dataclass
class ApplicationConfig:
    """
    agent_grpc_service: AgentServiceConfig
    consul: ConsulConfig
    messaging: MessagingConfig
    rabbitmq: RabbitMQConfig
    uds: UDSConfig
    """

    agent_grpc_service: AgentServiceConfig
    consul: ConsulConfig
    messaging: MessagingConfig
    rabbitmq: RabbitMQConfig
    uds: UDSConfig


class ConfigurationException(Exception):
    pass
