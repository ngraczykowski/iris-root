import pytest

from organization_name_knowledge.freetext.matching import get_names_from_org_name_markers
from organization_name_knowledge.freetext.parse import (
    _get_names_to_remove,
    _get_names_with_unique_bases,
    _get_valid_names,
)
from organization_name_knowledge.names.parse import parse_name


@pytest.mark.parametrize(
    "text, expected_names",
    [
        (
            "Office of the ABC Bank",
            [
                {"base": "ABC Bank", "source": "the ABC Bank"},
                {"base": "ABC Bank", "source": "ABC Bank"},
            ],
        ),
        (
            "There is some text about Bank of Scotland",
            [{"base": "Bank of Scotland", "source": "Bank of Scotland"}],
        ),
        (
            "The fall of the General Motors Group was just a part of crisis",
            # group is also a suffix
            [
                {"base": "General Motors", "source": "General Motors Group"},
                {"base": "Motors", "source": "Motors Group"},
            ],
        ),
    ],
)
def test_get_names_from_org_name_markers(text, expected_names):
    tokens = text.lower().split()
    names = get_names_from_org_name_markers(tokens)
    assert len(names) == len(expected_names)
    for name, expected in zip(names, expected_names):
        assert name.source.cleaned == expected["source"].lower()
        assert name.base.cleaned_name == expected["base"].lower()


@pytest.mark.parametrize(
    "names, expected_names_to_remove",
    [
        (["ABC Limited"], []),
        (["ABC Limited", "Limited xyz"], ["Limited xyz"]),
        (["Silent Eight Pte Ltd"], []),
        (["Silent Eight Pte Ltd", "Ltd 123 456"], ["Ltd 123 456"]),
    ],
)
def test_get_names_to_remove(names, expected_names_to_remove):
    names = [parse_name(name) for name in names]
    names_to_remove = _get_names_to_remove(names)
    assert len(names_to_remove) == len(expected_names_to_remove)
    for name_to_remove, expected_name_to_remove in zip(names_to_remove, expected_names_to_remove):
        assert name_to_remove.source.original == expected_name_to_remove


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


@pytest.mark.parametrize(
    "names, expected_names",
    [
        ([], []),
        (["abc"], []),
        (["XY Pte Ltd", "XY Limited"], ["XY Pte Ltd", "XY Limited"]),
        (["ABC Company Limited", "Company Limited ABC"], ["ABC Company Limited"]),
        (["Corporation of XYZ"], ["Corporation of XYZ"]),
        (["XYZ Corporation"], ["XYZ Corporation"]),
        (["ABC", "ABC Company"], ["ABC Company"]),
        (["Silent Eight Pte Ltd", "Silent Eight"], ["Silent Eight Pte Ltd"]),
        (["ABC Limited", "ABC DEF GHI JKL MNO Limited"], ["ABC Limited"]),
    ],
)
def test_get_valid_names(names, expected_names):
    names = [parse_name(name) for name in names]
    valid_names = _get_valid_names(names, base_tokens_upper_limit=3)
    assert len(valid_names) == len(expected_names)
    for valid_name, expected in zip(valid_names, expected_names):
        assert valid_name.source.original == expected
