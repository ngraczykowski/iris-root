import dataclasses
import logging
import os
from typing import Any, Mapping, Optional

import consul.aio
import requests


class ConsulServiceError(Exception):
    pass


@dataclasses.dataclass
class ConsulConfig:
    host: str
    port: int = 8500
    token: Optional[str] = None
    secret_path: Optional[str] = None
    trusted_ca: Optional[str] = None
    client_private_key: Optional[str] = None
    client_public_key_chain: Optional[str] = None

    def __post_init__(self):
        if self.port:
            self.port = int(self.port)
        else:
            self.port = 8500


class ConsulService:
    def __init__(self, config: ConsulConfig):
        self.logger = logging.getLogger("main").getChild("consul_service")
        self.consul = None
        self.consul_prefix = "discovery:///"
        config = self._update_consul_config_from_env(config)
        self.map = {}
        self.params = {}

        if all((config.trusted_ca, config.client_public_key_chain, config.client_private_key)):
            self.consul = consul.aio.Consul(
                host=config.host,
                port=config.port,
                token=config.token,
                verify=config.trusted_ca,
                cert=[
                    config.client_public_key_chain,
                    config.client_private_key,
                ],
                scheme="https",
            )
        else:
            self.consul = consul.aio.Consul(host=config.host, port=config.port, token=config.token)

    async def get_service(self, key: str, **kwargs) -> str:
        if self.consul_prefix in key:
            key = key.replace(self.consul_prefix, "")
        service = await self._get_service_information(key, **kwargs)
        self.logger.debug(f"{key} : {service}")
        return f"{service['ServiceAddress'] or 'localhost'}:{service['ServicePort']}"

    async def _get_service_information(self, key: str, **kwargs) -> Mapping[str, Any]:
        if not self.consul:
            raise Exception("Consul not configured")

        _, services = await self.consul.catalog.service(key, **kwargs)
        if not services:
            self.logger.error(f"Service {key} is not known")
            raise ConsulServiceError(f"Service {key} is not known")

        return services[0]

    async def get(self, key: str):
        _, value = self.consul.kv.get(key)
        return value["Value"] if value else None

    def get_secret(self, secret_name):
        try:
            return self.params[secret_name]
        except KeyError:
            pass
        try:
            return self.map[secret_name]
        except KeyError:
            if self.CONSUL_SECRET_PATH:
                try:
                    byte_secrets = self.c.kv.get(self.CONSUL_SECRET_PATH)[1]["Value"]
                    secrets = byte_secrets.decode().splitlines()
                    for secret in secrets:
                        left_side_ix = secret.find("=")
                        if left_side_ix == -1:
                            self.logger.warning("No '=' in secret")

                        variable_name = secret[:left_side_ix]
                        if variable_name == secret_name:
                            self.map[variable_name] = secret[left_side_ix + 1 :]
                            self.logger.debug(f"Got environment variable: {variable_name}")
                except (
                    requests.exceptions.InvalidURL,
                    requests.exceptions.ConnectionError,
                    ConsulServiceError,
                ):
                    raise ConsulServiceError("No valid consul connection")
            else:
                raise ConsulServiceError("No valid consul service secrets path")
        return self.map[secret_name]

    @staticmethod
    def _update_consul_config_from_env(config: ConsulConfig) -> ConsulConfig:
        return ConsulConfig(
            host=os.environ.get("CONSUL_HTTP_ADDR") if not config.host else config.host,
            token=os.environ.get("CONSUL_HTTP_TOKEN") if not config.token else config.token,
            secret_path=os.environ.get("CONSUL_SECRET_PATH")
            if not config.secret_path
            else config.secret_path,
            trusted_ca=os.environ.get("CONSUL_CACERT")
            if not config.trusted_ca
            else config.trusted_ca,
            client_private_key=os.environ.get("CONSUL_CLIENT_KEY")
            if not config.client_private_key
            else config.client_private_key,
            client_public_key_chain=os.environ.get("CONSUL_CLIENT_CERT")
            if not config.client_public_key_chain
            else config.client_public_key_chain,
        )

    def __getattr__(self, name):
        try:
            return object.__getattribute__(self, name)
        except AttributeError:
            return self.get_secret(name)
