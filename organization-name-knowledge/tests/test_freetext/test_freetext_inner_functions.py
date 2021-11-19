import pytest

from organization_name_knowledge.freetext.parse import _get_names_with_unique_bases
from organization_name_knowledge.names.parse import parse_name


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
