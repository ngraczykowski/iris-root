import glob
import logging
import os

import consul
from omegaconf import OmegaConf, dictconfig

import etl_pipeline

CONFIG_APP_DIR = os.environ["CONFIG_APP_DIR"]
logger = logging.getLogger("main")


class ConsulServiceError(Exception):
    pass


class ConsulServiceConfig:
    def __init__(self) -> None:
        service = OmegaConf.load(os.path.join(CONFIG_APP_DIR, "service", "service.yaml"))
        host = getattr(service, "CONSUL_HOSTNAME", None)
        port = getattr(service, "CONSUL_PORT", None)
        cert = getattr(service, "CONSUL_CLIENT_CERT", None)
        token = getattr(service, "CONSUL_ACL_TOKEN", None)

        self.c = consul.Consul(host=host, port=port, cert=cert, token=token)
        self.map = {}

    def __getattr__(self, name):
        try:
            return object.__getattribute__(self, name)
        except AttributeError:
            return self.get_secret(name)

    def get_secret(self, secret_name):
        try:
            return self.map[secret_name]
        except KeyError:
            byte_secrets = self.c.kv.get("mike/etl_service/secrets")[1]["Value"]
            secrets = byte_secrets.decode().splitlines()
            for secret in secrets:
                left_side_ix = secret.find("=")
                if left_side_ix == -1:
                    logger.warning("No '=' in secret")

                variable_name = secret[:left_side_ix]
                if variable_name == secret_name:
                    self.map[variable_name] = secret[left_side_ix + 1 :]
                    logger.debug(f"Got environment variable: {variable_name}")
        return self.map[variable_name]

    def get_service(self, service_name):
        service_name = service_name.replace("discovery:///", "")
        address = self.c.catalog.service(service_name)[1][0]["ServiceAddress"]
        port = self.c.catalog.service(service_name)[1][0]["ServicePort"]
        return f"{address}:{port}"


def load_agent_config(alert_config):
    parsed_agent_config = {}
    for agent_name, agent_config in dict(alert_config).items():
        particular_agent_config = dict(agent_config)
        parsed_agent_config[agent_name] = {}
        for new_key in particular_agent_config:
            parsed_agent_config[agent_name][new_key] = []
            for element in particular_agent_config[new_key]:
                elements = element.split(".")
                for element in elements:
                    if element not in pipeline_config.cn.values():
                        logger.warning(
                            f"Field in agent configuration for: {element} is not registered field in pipeline.yaml"
                        )
                parsed_agent_config[agent_name][new_key].append(elements[-1])

    return parsed_agent_config, alert_config


def load_agent_configs():
    alert_agents_config = {
        "alert_type": OmegaConf.load(os.path.join(CONFIG_APP_DIR, "agents", "agents.yaml"))
    }

    validate_agents_fields(alert_agents_config)

    alert_agents_config = {
        alert_type: load_agent_config(config)
        for alert_type, config in alert_agents_config.items()
        if config
    }
    return alert_agents_config


class Pipeline:
    def __init__(self):
        self.config = None
        self.cn = None
        self.load_configs()

    def load_configs(self):
        config = OmegaConf.load(os.path.join(CONFIG_APP_DIR, "pipeline", "pipeline.yaml"))
        self.config = config.PIPELINE
        self.cn = config.PAYLOAD_KEYS

    def reload_pipeline_config(self):
        self.load_configs()


def get_fields(dict_):
    raw_fields = []

    # extract all the fields
    for key in dict_:

        if isinstance(dict_[key], dictconfig.DictConfig):
            new_fields = get_fields(dict_[key])
            raw_fields.extend(new_fields)
        else:
            raw_fields.extend(dict_[key])

    raw_fields_no_duplicates = list(set(raw_fields))

    fields = [
        i.split(".")[-1]
        for i in raw_fields_no_duplicates
        if "alertedParty.inputRecordHist.inputRecords.INPUT_FIELD" not in i
    ]

    return fields


def check_fields_in_files(fields, file_patterns):
    files = []
    for file_pattern in file_patterns:
        files.extend(glob.glob(file_pattern, recursive=True))
    for file in files:
        with open(file, "r", encoding="utf-8") as f:
            text = f.read()

        # check which fields are present in the file
        fields_present = [x for x in fields if x in text]
        if fields_present:
            logger.debug(
                f"{len(fields_present)} field(s) found in {file}, removing it/them from the list..."
            )
            fields = list(set(fields) - set(fields_present))
        else:
            logger.debug(f"No fields found in {file}")

    # return the fields that have not been found in any file
    return fields


def validate_agents_fields(alert_agents_config):
    etl_pipeline_dir = os.path.dirname(etl_pipeline.__file__)
    dirs = [etl_pipeline_dir, etl_pipeline_dir.replace("etl_pipeline", "pipelines")]

    file_patterns = [f"{dir}/**/*.py" for dir in dirs]
    fields = get_fields(alert_agents_config["alert_type"])
    fields_not_found = check_fields_in_files(fields, file_patterns)

    if fields_not_found:
        logger.warning("The following fields have not been found in any file:")
        logger.warning(fields_not_found)
    else:
        logger.info("All the fields have been found in some file.")


service_config = OmegaConf.load(os.path.join(CONFIG_APP_DIR, "service", "service.yaml"))

for key in service_config:
    try:
        if service_config[key].startswith("discovery:///"):

            service_config[key] = ConsulServiceConfig().get_service(service_config[key])
    except AttributeError:
        continue


pipeline_config = Pipeline()
