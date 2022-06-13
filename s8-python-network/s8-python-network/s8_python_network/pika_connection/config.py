import dataclasses
from typing import Any, Dict, Optional


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
