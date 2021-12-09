from pathlib import Path

from requests import Session

from utils import config_service
from utils import consul_service

http = Session()
config = config_service.get_serp_client_config()
http.verify = config.ca_certs_path
http.cert = (config.user_cert_chain_path, config.user_cert_key_path)


def import_tree(dt_path: str):
    address, port = consul_service.get_service_address_and_port("governance")
    url = "https://{}:{}/rest/governance/api/v1/migrate/decision-tree".format(address, port)
    return http.post(url, files={'file': Path(dt_path).read_text()}).json()
