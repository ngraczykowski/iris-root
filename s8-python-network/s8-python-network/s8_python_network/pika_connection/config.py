import copy
import dataclasses
from typing import Generator, Optional


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
    port: Optional[int] = None
    addresses: Optional[str] = None
    ssl_options: Optional[RMQSSLOptions] = None

    Note: address, host & port are 'Optional', but at least one of: address, or host + port
    must be set to connect with Rabbit MQ !
    """

    login: str
    password: str
    virtualhost: str
    host: Optional[str] = None
    port: Optional[int] = None
    addresses: Optional[str] = None
    ssl_options: Optional[RMQSSLOptions] = None

    def __post_init__(self):
        if self.port:
            try:
                self.port = int(self.port)
            except ValueError:
                raise ValueError("Port must be a number!")

    def generate(self) -> Generator["RabbitMQConfig", None, None]:
        if self.host and self.port:
            yield self

        if self.addresses:
            for address in self.addresses.split(","):
                rmq_config = copy.copy(self)
                rmq_config.host, rmq_config.port = address.split(":")
                rmq_config.port = int(rmq_config.port)
                yield rmq_config

    def all_required(self) -> bool:
        """This method verifies existence all fields required to connect to RabbitMQ.
        'addresses' is not checked, since it should be parsed i.e. using .generate() method
        """

        return all(
            (
                elem is not None
                for elem in (self.login, self.password, self.virtualhost, self.host, self.port)
            )
        )
