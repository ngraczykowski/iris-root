import pytest

from ..compare import compare


@pytest.mark.parametrize(
    ("name", "expected_legal_term"),
    (
        ("Google", ()),
        ("EISOO Information Technology Corp.", ("corp",)),
        ("Atlassian Corporation Plc", ("corporation", "plc")),
    ),
)
def test_legal_term(name, expected_legal_term):
    _, (information, _) = compare(name, "AAAA")
    assert information["legal"] == expected_legal_term


@pytest.mark.parametrize(
    ("name", "expected_common_suffixes"),
    (
        ("Google", ()),
        ("EISOO Information Technology Corp.", ("information", "technology")),
    ),
)
def test_common_suffixes(name, expected_common_suffixes):
    _, (information, _) = compare(name, "AAAA")
    assert information["common_suffixes"] == expected_common_suffixes


@pytest.mark.parametrize(
    ("name", "expected_base"),
    (
        ("Google", ("google",)),
        ("EISOO Information Technology Corp.", ("eisoo",)),
        (
            "Industrial and Commercial Bank of China",
            ("industrial", "and", "commercial", "bank", "of", "china"),
        ),
    ),
)
def test_base(name, expected_base):
    _, (information, _) = compare(name, "AAAA")
    assert information["base"] == expected_base


@pytest.mark.parametrize(
    ("name", "expected_country"),
    (
        ("Google", ()),
        ("Google (UK)", ("uk",)),
        ("(UK) Google", ("uk",)),
        ("Google (UK) Facebook", ("uk",)),
        ("Google (United Kingdom)", ("united kingdom",)),
        ("(UK) Google (China)", ("uk", "china")),
        ("(Facebook) Google", ()),
        ("(France) (Facebook) Google", ("france",)),
    ),
)
def test_country(name, expected_country):
    _, (information, _) = compare(name, "AAAA")
    assert set(information["countries"]) == set(expected_country)


@pytest.mark.parametrize(
    ("name", "expected_parenthesis"),
    (
        ("Google", ()),
        ("(UK) Google (China)", ()),
        ("(Facebook) Google", ("facebook",)),
        ("(France) (Facebook) Google", ("facebook",)),
    ),
)
def test_parenthesis(name, expected_parenthesis):
    _, (information, _) = compare(name, "AAAA")
    assert set(information["parenthesis"]) == set(expected_parenthesis)
