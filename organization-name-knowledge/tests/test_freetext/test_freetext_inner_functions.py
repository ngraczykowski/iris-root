import pytest

from organization_name_knowledge.freetext.parse import (
    _get_long_names_substrings_based_names,
    _get_names_with_unique_bases,
)
from organization_name_knowledge.names.parse import parse_name


@pytest.mark.parametrize(
    "name, expected_output",
    [
        ("Silent Eight Pte Ltd", ["Eight"]),
        ("KGHM SA", []),
        ("ABC DEFG HIJK corp", ["DEFG HIJK", "HIJK"]),
    ],
)
def test_get_long_names_substrings_based_names(name, expected_output):
    name_information_seq = [parse_name(name)]
    substring_based_names = _get_long_names_substrings_based_names(name_information_seq)
    assert len(substring_based_names) == len(expected_output)
    for substring_based_name, expected in zip(substring_based_names, expected_output):
        assert substring_based_name.base.original_name == expected


@pytest.mark.parametrize(
    "names, expected_names",
    [
        (["abc"], ["abc"]),
        (["abc", "abc", "abc", "abc"], ["abc"]),
        (["Non duplicated name LTD"], ["Non duplicated name"]),
        (["Silent Eight Pte Ltd", "Silent Eight Company"], ["Silent Eight"]),
        (["ABC corp", "XYZ corp", "ABC Limited"], ["ABC", "XYZ"]),
    ],
)
def test_get_names_with_unique_bases(names, expected_names):
    names = [parse_name(name) for name in names]
    names = _get_names_with_unique_bases(names)
    assert len(names) == len(expected_names)
    for name, expected in zip(names, expected_names):
        assert name.base.original_name == expected
