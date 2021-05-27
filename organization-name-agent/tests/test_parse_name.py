import pytest

from company_name.names.parse_name import parse_name


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
    information = parse_name(name)
    assert information.base == expected_base


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
    information = parse_name(name)
    assert set(information.parenthesis) == set(expected_parenthesis)


@pytest.mark.parametrize(
    ("name", "expected"),
    (
        (
            "THOMAS J. COLEMAN AND COMPANY LIMITED",
            {
                "base": ("thomas", "j", "coleman", "and", "company"),
                "legal": ("limited",),
            },
        ),
        ("MS DESIGN & CONSTRUCTIONS", {"base": ("ms", "design", "&", "constructions")}),
        (
            "group and AAAAAAAA",
            {"common_prefixes": (), "base": ("group", "and", "aaaaaaaa")},
        ),
    ),
)
def test_do_not_divide_when_and(name, expected):
    information = parse_name(name)
    print(information)
    for key, value in expected.items():
        assert set(getattr(information, key)) == set(value)


@pytest.mark.parametrize(
    ("name", "expected"),
    (
        (
            "TONTINE ROOMS HOLDING COMPANY LIMITED - THE",
            {"legal": ("company", "limited")},
        ),
    ),
)
def test_the_on_the_end_do_not_destroy_cutting_other_information(name, expected):
    information = parse_name(name)
    print(information)
    for key, value in expected.items():
        assert set(getattr(information, key)) == set(value)
