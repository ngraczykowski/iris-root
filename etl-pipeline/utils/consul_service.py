import random

from consul import Consul

from utils import config_service


class SelectServicePolicy:
    def apply(self, services: [any]) -> any:
        pass


class RandomSelectServicePolicy(SelectServicePolicy):
    def apply(self, services: [any]) -> any:
        random_index = random.randint(0, len(services) - 1)
        return services[random_index]


default_select_service_policy = RandomSelectServicePolicy()


def get_service_address_and_port(
        service_name: str,
        policy: SelectServicePolicy = default_select_service_policy) -> (str, int):
    services = _get_services(service_name)

    if len(services) == 0:
        raise Exception('Service "{}" is not registered in Consul.'.format(service_name))

    service = policy.apply(services)
    return service['ServiceAddress'], service['ServicePort']


def _get_services(service_name: str):
    _, services = consul.catalog.service(service_name)
    return services


def _create_consul():
    config = config_service.get_serp_client_config()
    return Consul(
        host=config.consul_host,
        port=int(config.consul_port),
        scheme="https",
        verify=config.ca_certs_path,
        cert=(config.user_cert_chain_path, config.user_cert_key_path)
    )


consul = _create_consul()
