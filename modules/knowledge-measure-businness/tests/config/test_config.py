import os
from dataclasses import asdict

import pytest
import yaml

from business_layer.config.config import Config
from business_layer.errors import ConfigurationError

RESOURCES_PATH = "tests/config/resources/"


def test_config():
    config_params = _read_yaml_config("all.yaml")
    config = Config(config_parameters=config_params)

    expected_decisions = {"PTP", "FP", "MI"}
    assert config.decisions == expected_decisions

    expected_comments = {
        "PTP": {"verbal": "Potential True Positive", "type": "positive_comment"},
        "FP": {"verbal": "False Positive", "type": "negative_comment"},
        "MI": {"verbal": "Manual Investigation", "type": None},
    }
    for decision, comment in expected_comments.items():
        assert asdict(config.comments[decision]) == comment

    assert len(config.knowledge_toolbox.standard_features_configs) == 2
    assert len(config.knowledge_toolbox.custom_features) == 1
    assert len(config.measure_toolbox.standard_features_configs) == 2
    assert len(config.measure_toolbox.custom_features) == 3  # one for each of 3 fields

    assert len(config.requested_mappings) == 8


@pytest.mark.parametrize(
    "config_file_name, expected_error_message",
    [
        ("empty.yaml", "Invalid configuration file content!"),
        ("invalid_domain_source.yaml", "Invalid config for domain abc_agent"),
        ("invalid_domain_type.yaml", "Invalid config for domain abc_agent!"),
        (
            "non_existing_custom_feature.yaml",
            "Cannot import required module business_layer.custom_knowledge.abc_agent",
        ),
    ],
)
def test_config_errors(config_file_name, expected_error_message):
    with pytest.raises(ConfigurationError, match=expected_error_message):
        Config(_read_yaml_config(config_file_name))


def _read_yaml_config(file_name: str):
    with open(os.path.join(RESOURCES_PATH, file_name)) as file:
        config_params = yaml.safe_load(file)
    return config_params
