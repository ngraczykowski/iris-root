import glob
import logging
import os
import re

import consul
from omegaconf import OmegaConf, dictconfig

import etl_pipeline

CONFIG_APP_DIR = os.environ["CONFIG_APP_DIR"]
logger = logging.getLogger("main")


class ConsulServiceError(Exception):
    pass


class DatasetConfigError(Exception):
    pass


class ConsulServiceConfig:
    def __init__(self) -> None:
        service = OmegaConf.load(os.path.join(CONFIG_APP_DIR, "service", "service.yaml"))
        host = getattr(service, "CONSUL_HOSTNAME", None)
        port = getattr(service, "CONSUL_PORT", None)
        cert_file = getattr(service, "GRPC_CLIENT_TLS_CA", None)
        token = getattr(service, "CONSUL_ACL_TOKEN", None)
        self.c = consul.Consul(host=host, port=port, cert=cert_file, token=token)
        self.map = {}
        self.params = None
        self.reload()

    def __getattr__(self, name):
        try:
            return object.__getattribute__(self, name)
        except AttributeError:
            return self.get_secret(name)

    def reload(self):
        self.params = OmegaConf.load(os.path.join(CONFIG_APP_DIR, "service", "service.yaml"))
        for key in self.params:
            try:
                if self.params[key].startswith("discovery:///"):
                    self.params[key] = self.get_service(self.params[key])
            except AttributeError:
                continue

    def get_secret(self, secret_name):
        try:
            return self.params[secret_name]
        except KeyError:
            pass
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
        return self.map[secret_name]

    def get_service(self, service_name):
        service_name = service_name.replace("discovery:///", "")
        try:
            address = self.c.catalog.service(service_name)[1][0]["ServiceAddress"]
            port = self.c.catalog.service(service_name)[1][0]["ServicePort"]
        except (IndexError, KeyError):
            return None
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

    check_for_duplicate_fields(alert_agents_config)
    validate_agents_fields(alert_agents_config)

    alert_agents_config = {
        alert_type: load_agent_config(config)
        for alert_type, config in alert_agents_config.items()
        if config
    }
    return alert_agents_config


class Pipeline:
    def __init__(self, filename=os.path.join(CONFIG_APP_DIR, "pipeline", "pipeline.yaml")):
        self.config = None
        self.cn = None
        self.dataset_config = None
        self.load_configs(filename)

    def load_configs(self, filename):
        config = OmegaConf.load(filename)
        self.config = config.PIPELINE
        self.cn = config.PAYLOAD_KEYS
        self.dataset_config = self.load_dataset_config(config.DATASET_CONFIG)

    def load_dataset_config(self, dataset_scope):
        dataset_config = {}
        for key, value in dataset_scope.items():
            dataset_config[re.compile(key)] = value
        return DatasetConfig(dataset_config)

    def reload_pipeline_config(self):
        self.load_configs()


def check_duplicates_for_agent_configuration(agent_name, agent_config):
    for category in agent_config.keys():
        for field in set(agent_config[category]):
            if agent_config[category].count(field) > 1:
                logger.warning(
                    f"The following field in agents.yaml appears more than once: {agent_name} -> {category} -> {field}"
                )


class DatasetConfig:
    def __init__(self, config):
        self.config = config

    def get(self, dataset_name):
        for key, value in self.config.items():
            if key.match(dataset_name):
                return value
        raise DatasetConfigError(f"Dataset {dataset_name} is not expected by ETL")


def check_for_duplicate_fields(alert_agents_config):
    for agent in alert_agents_config["alert_type"]:
        check_duplicates_for_agent_configuration(agent, alert_agents_config["alert_type"][agent])


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
        fields_present = [field for field in fields if field in text]
        if fields_present:
            fields = list(set(fields) - set(fields_present))
    if fields:
        logger.debug(f"Check if transformation exist for those fields {fields}")
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


pipeline_config = Pipeline()
