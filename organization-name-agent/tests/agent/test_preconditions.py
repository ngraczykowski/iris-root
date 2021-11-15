import contextlib
import pathlib
import tempfile
from typing import List, Mapping

import pytest
import yaml
from agent_base.utils.config import Config, ConfigurationException
from organization_name_knowledge import parse

from company_name.solution.name_preconditions import NamePreconditions


@contextlib.contextmanager
def get_name_preconditions(configuration: Mapping) -> NamePreconditions:
    with tempfile.TemporaryDirectory() as t:
        configuration_dir = pathlib.Path(t)
        with open(configuration_dir / NamePreconditions.configuration_file_name, "wt") as f:
            yaml.dump(configuration, f)

        config = Config(configuration_dirs=(configuration_dir,))
        yield NamePreconditions(config)


@pytest.mark.parametrize(
    ("configuration", "name"),
    (
        (
            {},
            "Mappec Materiales Productos Poliméricos y Elementos de Construcción, S.A. de C.V.",
        ),  # no length set
        (
            {"max_length": 10},
            "google",
        ),
        (
            {"max_length": 68},
            "PAPELERA LOS PORTALES Y SERVICIOS MULTIPLES SOCIEDAD ANONIMA CERRADA",
        ),  # exact
    ),
)
def test_under_max_length(configuration: Mapping[str, int], name: str):
    with get_name_preconditions({"length": configuration}) as name_preconditions:
        assert name_preconditions.preconditions_met(parse(name))


@pytest.mark.parametrize(
    ("configuration", "name"),
    (
        (
            {"max_length": 2},
            "google",
        ),
        (
            {"max_length": 50},
            "PremAir Ipari, Kereskedelmi és Szolgáltató Korlátolt Felelősségű Társaság",
        ),
        (
            {"max_length": 50, "max_unique_tokens": 100},
            "EKOMAK ENDUSTRIYEL KOMPRESOR VE MAKINA SANAYI VE TICARET ANONIM SIRKET",
        ),  # with other checks
    ),
)
def test_over_max_length(configuration: Mapping[str, int], name: str):
    with get_name_preconditions({"length": configuration}) as name_preconditions:
        assert not name_preconditions.preconditions_met(parse(name))


@pytest.mark.parametrize(
    ("configuration", "name"),
    (
        (
            {},
            "XXXX",
        ),
        (
            {"without_part": []},
            "XXXX",
        ),
        (
            {"without_part": ["XXXX"]},
            "XXX",
        ),
        (
            {"without_part": ["XXXX"]},
            "XX XX",
        ),
        (
            {"without_token": ["XXXX"]},
            "XXXXX",
        ),
    ),
)
def test_inclusion_not_in_name(configuration: Mapping[str, List[str]], name: str):
    with get_name_preconditions({"inclusion": configuration}) as name_preconditions:
        assert name_preconditions.preconditions_met(parse(name))


@pytest.mark.parametrize(
    ("configuration", "name"),
    (
        (
            {"without_part": ["XXXX"]},
            "xxxx",
        ),
        (
            {"without_part": ["XXXX"]},
            "XXXXXX",
        ),
        (
            {"without_part": ["XXXX"]},
            "xxxxxx",
        ),
        (
            {"without_token": ["XXXX"]},
            "XXXX",
        ),
        (
            {"without_part": ["XXXX"], "without_token": ["XXXX"]},
            "XXXXX",
        ),
    ),
)
def test_inclusion_matching_name(configuration: Mapping[str, List[str]], name: str):
    with get_name_preconditions({"inclusion": configuration}) as name_preconditions:
        assert not name_preconditions.preconditions_met(parse(name))


@pytest.mark.parametrize(
    ("configuration", "name"),
    (
        (
            {},
            "google",
        ),
        (
            {"acceptable_alphabets": [], "min_acceptable_fraction": 0},
            "google",
        ),
        (
            {"acceptable_alphabets": ["latin", "greek"], "min_acceptable_fraction": 0.5},
            "google",
        ),
        (
            {"acceptable_alphabets": ["latin"], "min_acceptable_fraction": 0},
            "которых не становятся их участниками",
        ),
        (
            {"acceptable_alphabets": ["latin"], "min_acceptable_fraction": 0.5},
            "которых company",
        ),
    ),
)
def test_alphabet(configuration: Mapping, name: str):
    with get_name_preconditions({"alphabet": configuration}) as name_preconditions:
        assert name_preconditions.preconditions_met(parse(name))


@pytest.mark.parametrize(
    ("configuration", "name"),
    (
        (
            {"acceptable_alphabets": [], "min_acceptable_fraction": 1},
            "google",
        ),
        (
            {"acceptable_alphabets": ["chinese"], "min_acceptable_fraction": 1},
            "google",
        ),
        (
            {"acceptable_alphabets": ["latin"], "min_acceptable_fraction": 0.3},
            "которых не становятся их участниками",
        ),
        (
            {"acceptable_alphabets": ["latin"], "min_acceptable_fraction": 0.3},
            "21347512 468712 123678",
        ),
    ),
)
def test_alphabet_precondition_not_met(configuration: Mapping, name: str):
    with get_name_preconditions({"alphabet": configuration}) as name_preconditions:
        assert not name_preconditions.preconditions_met(parse(name))


@pytest.mark.parametrize(
    "configuration",
    (
        {"unknown_section": {}},
        {"length": {"unknown_key": 15}},
        {"alphabet": {"acceptable_alphabets": []}},
    ),
)
def test_incorrect_configuration(configuration: Mapping):
    with pytest.raises(ConfigurationException):
        with get_name_preconditions(configuration):
            pass
