import json
import pathlib
import tempfile

import pytest

from company_name.compare import parse_name
from company_name.knowledge_base import KnowledgeBase


@pytest.mark.parametrize(
    ("name", "expected_base"),
    (
        ("7th avenue", ("seventh", "avenue")),
        ("7 avenue", ("7", "avenue")),
        ("7thavenue", ("7thavenue",)),
    ),
)
def test_simple_mapping(name, expected_base):
    information = parse_name(name)
    assert information.base.cleaned_tuple == expected_base


@pytest.mark.parametrize(
    ("words_mapping", "name", "expected_base"),
    (
        ({}, "7th avenue", ("7th", "avenue")),
        ({"7": "funny"}, "7th avenue", ("7th", "avenue")),
        ({"7th": "funny"}, "7th avenue", ("funny", "avenue")),
    ),
)
def test_mapping_from_external_file(words_mapping, name, expected_base):
    with tempfile.TemporaryDirectory() as t:

        knowledge_dir = pathlib.Path(t)
        with open(knowledge_dir / "words_mapping.json", "wt") as f:
            json.dump(words_mapping, f)
        KnowledgeBase.set_additional_source_paths(dirs=(knowledge_dir,))

        information = parse_name(name)
        assert information.base.cleaned_tuple == expected_base

        KnowledgeBase.set_additional_source_paths(dirs=())
