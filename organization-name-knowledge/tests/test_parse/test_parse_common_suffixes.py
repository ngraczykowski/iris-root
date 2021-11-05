import pytest

from organization_name_knowledge.names.parse.parse import parse_name


@pytest.mark.parametrize(
    ("name", "expected_common_suffixes"),
    (
        ("Google", ()),
        ("EISOO Information Technology Corp.", ("information", "technology")),
    ),
)
def test_common_suffixes(name, expected_common_suffixes):
    information = parse_name(name)
    assert information.common_suffixes == expected_common_suffixes
