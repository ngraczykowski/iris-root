import dataclasses
from typing import Optional


@dataclasses.dataclass
class RMQSSLOptions:
    """
    cafile: str
    keyfile: str
    certfile: str
    verify: int

    verify param: 1 or 0 - to verify certificate or not
    """

    cafile: str
    keyfile: str
    certfile: str
    verify: int


@dataclasses.dataclass
class RabbitMQConfig:
    """
    login: str
    password: str
    virtualhost: str
    host: Optional[str] = None
    port: Optional[str] = None
    addresses: Optional[str] = None
    ssl_options: Optional[RMQSSLOptions] = None

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
