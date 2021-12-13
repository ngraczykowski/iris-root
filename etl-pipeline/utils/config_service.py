import contextlib
import os
from pathlib import Path
from typing import Sequence

from serppythonclient.stub import YamlStubProviderPropertiesLoader
from serppythonclient.yml import YamlLoader

ROOT_DIR = str(Path(__file__).parent.parent.absolute())
DEFAULT_CONFIG_DIR = str(Path(ROOT_DIR).joinpath("config").joinpath("default"))
DEFAULT_WORKING_DIR = str(Path(ROOT_DIR).joinpath("data/e2e"))
CONFIG_DIR = "./config/default/"
WORKING_DIR = os.getenv("MOAPOV_WORKING_DIR_PATH", DEFAULT_WORKING_DIR)

config_locations = [
    Path(CONFIG_DIR).joinpath("configuration.yml"),
    Path(CONFIG_DIR).joinpath("../default/configuration.yml"),
]


class MissingConfigurationFileException(Exception):
    def __init__(self):
        super().__init__(f"Missing configuration.yml file. Expected locations: {config_locations}")


CONFIG_PATH = None
for config_location in config_locations:
    if config_location.exists():
        CONFIG_PATH = str(config_location)

if CONFIG_PATH is None:
    raise MissingConfigurationFileException()

PIPELINE_PATH = os.getenv("S8_CONFIG_PIPELINE_PATH", CONFIG_PATH)

DEFAULT_PROCESSING_POOL_SIZE = 2

with open(CONFIG_PATH) as file:
    config = YamlLoader.get_instance().load(file)
    customer_specific_config = config["customer_specific"]
    print(config)

with open(PIPELINE_PATH) as f:
    pipeline_config = YamlLoader.get_instance().load(f)
    print(pipeline_config)


def get_dataset_type():
    return config["dataset"]


def get_spark_config():
    return pipeline_config["spark"]


def get_processing_pool_size():
    if "processing_pool_size" in config:
        return config["processing_pool_size"]
    return DEFAULT_PROCESSING_POOL_SIZE


def get_working_dir():
    working_dir = Path(WORKING_DIR)
    working_dir.mkdir(parents=True, exist_ok=True)
    return str(working_dir)


def get_tmp_dir():
    return __tmp_dir


def get_tmp_file_path(filename):
    return str(Path(get_tmp_dir()).joinpath(filename))


def __get_config_dir(dir):
    dir_path = config[dir]

    if Path(dir_path).is_absolute():
        return dir_path
    else:
        return str(Path(ROOT_DIR).joinpath(dir_path))


def get_serp_client_config():
    return YamlStubProviderPropertiesLoader.load(CONFIG_PATH)


def __get_file_path(dir_property: str, files_property: str, path_property: str):
    files_dir = Path(get_working_dir()).joinpath(config[dir_property])
    files_dir.mkdir(parents=True, exist_ok=True)
    file_path = files_dir.joinpath(config[files_property][path_property])

    return str(file_path)


class PropertyNotFoundException(Exception):
    def __init__(self, property_path, key):
        super().__init__(f'Key not found "{key}". Property Path: "{property_path}"')


def __get_property_value(property_path: str):
    keys = property_path.split(".")
    current = config

    for key in keys:
        if key in current:
            current = current[key]
        else:
            raise PropertyNotFoundException(property_path, key)

    return current


def __get_file_paths(dir_property: str, files_property: str):
    working_dir = get_working_dir()
    files_dir = __get_property_value(dir_property)
    file_path = __get_property_value(files_property)

    paths = []
    if file_path:
        for path in file_path:
            paths.append(str(Path(working_dir).joinpath(files_dir).joinpath(path)))

    return paths


def get_input_file_path(path_property: str):
    return __get_file_path("input_files_dir", "input_files", path_property)


def get_artifact_file_path(path_property: str):
    return __get_file_path("artifact_files_dir", "artifact_files", path_property)


def get_crucial_artifact_file_path(path_property: str):
    return __get_file_path("crucial_artifact_files_dir", "crucial_artifact_files", path_property)


def get_pipeline_configuration():
    return pipeline_config["pipeline"]


def get_agents_configuration():
    return config["agents"]


def get_agent_configuration(agent_name: str):
    return config["agents"][agent_name]


def get_location_for(dir_property: str, file_name: str):
    working_dir = get_working_dir()
    files_dir = config[dir_property]

    return str(Path(working_dir).joinpath(files_dir).joinpath(file_name))


def get_input_location_for(file_name: str):
    return get_location_for("input_files_dir", file_name)


def get_artifact_location_for(file_name: str):
    return get_location_for("artifact_files_dir", file_name)


def get_crucial_artifact_location_for(file_name: str):
    return get_location_for("crucial_artifact_files_dir", file_name)


def get_exportable_location_for(file_name: str):
    return get_location_for("exportable_files_dir", file_name)


def get_documented_business_questions():
    return customer_specific_config["documented_business_questions"]


def get_sentiment_blacklists():
    return customer_specific_config.get("sentiment_blacklists", {})


def get_external_analyst_sentiments_paths():
    return __get_file_paths("input_files_dir", "customer_specific.external_analyst_sentiments_files")


def get_analyst_resoning_config_file():
    return str(Path(CONFIG_DIR).joinpath(customer_specific_config["analyst_resoning_config_file"]))


def get_coverage_score_config_file():
    return str(Path(CONFIG_DIR).joinpath(customer_specific_config["coverage_score_config_file"]))


def get_exportable_configurations():
    return config["exportable_files"]


def get_csv_options():
    return customer_specific_config["csv_options"]


def get_custom_configuration():
    return config.get("custom", {})


@contextlib.contextmanager
def config_path_in_environment(environment_var="S8_CONFIG_DIR_PATH"):
    old_value = os.environ.get(environment_var)
    os.environ[environment_var] = CONFIG_DIR
    try:
        yield
    finally:
        if old_value is None:
            del os.environ[environment_var]
        else:
            os.environ[environment_var] = old_value


def get_agents_call():
    return config["agents-call"]


__tmp_dir = __get_config_dir("tmp_dir")


def load_steps(name) -> Sequence[str]:
    path = Path(ROOT_DIR).joinpath(f"conf/steps/{name}.yml")
    with open(str(path)) as f:
        return YamlLoader.get_instance().load(f)["steps"]
