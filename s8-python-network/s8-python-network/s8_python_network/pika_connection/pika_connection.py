import logging

import aio_pika

from s8_python_network.pika_connection.config import RabbitMQConfig


class BasePikaConnection:
    def __init__(
        self,
        connection_configuration: RabbitMQConfig,
        max_requests_to_worker: int,
        use_ssl: bool = False,
    ):
        self.connection_configuration = connection_configuration
        self.connection, self.channel = None, None

        self.logger = logging.getLogger("main").getChild("pika_connection")
        self.max_requests_to_worker = max_requests_to_worker
        self.ssl = use_ssl

    def start(self) -> None:
        ...

    async def stop(self) -> None:
        ...

    async def set_connection(self):
        if self.ssl:
            ssl_options = self.connection_configuration.ssl_options
            if not ssl_options or not all(
                (ssl_options.cafile, ssl_options.keyfile, ssl_options.certfile, ssl_options.verify)
            ):
                raise ValueError(
                    "No ssl connection parameters in config "
                    "- add 'rabbitmq.ssl_options' section to application.yaml "
                    "or set relevant environment variables"
                )
            url = "".join(
                map(
                    str,
                    (
                        "amqps://",
                        self.connection_configuration.login,
                        ":",
                        self.connection_configuration.password,
                        "@",
                        self.connection_configuration.host,
                        ":",
                        self.connection_configuration.port,
                        "/",
                        self.connection_configuration.virtualhost,
                        "?cafile=",
                        self.connection_configuration.ssl_options.cafile,
                        "&keyfile=",
                        self.connection_configuration.ssl_options.keyfile,
                        "&certfile=",
                        self.connection_configuration.ssl_options.certfile,
                        "&no_verify_ssl=",  # that's how aio-pika named it ...
                        self.connection_configuration.ssl_options.verify,
                    ),
                )
            )
            self.connection: aio_pika.RobustConnection = await aio_pika.connect_robust(url=url)
        else:
            self.connection: aio_pika.RobustConnection = await aio_pika.connect_robust(
                host=self.connection_configuration.host,
                port=self.connection_configuration.port,
                login=self.connection_configuration.login,
                password=self.connection_configuration.password,
                virtualhost=self.connection_configuration.virtualhost,
            )

    async def set_channel(self):
        if not self.connection:
            raise ValueError("PikaConnection.connection not set - cannot set a channel!")
        self.channel: aio_pika.Channel = await self.connection.channel()
        await self.channel.set_qos(prefetch_count=self.max_requests_to_worker)
