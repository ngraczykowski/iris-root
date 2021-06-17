import pytest

from company_name.names.parse.parse import parse_name


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
            {
                "base": ("tontine", "rooms"),
                "common_suffixes": ("holding",),
                "legal": ("company", "limited"),
                "other": ("the",),
            },
        ),
        (
            "Bank of Nova Scotia, The",
            {"base": ("bank", "of", "nova", "scotia"), "other": ("the",)},
        ),
        (
            "Corporation of the City of London, The",
            {
                "base": ("corporation", "of", "the", "city", "of", "london"),
                "other": ("the",),
            },
        ),
    ),
)
def test_the_on_the_end_do_not_destroy_cutting_other_information(name, expected):
    information = parse_name(name)
    print(information)
    for key, value in expected.items():
        assert set(getattr(information, key)) == set(value)


@pytest.mark.skip
@pytest.mark.parametrize(
    ("name", "expected"),
    (
        (
            "M2A SOLUTIONS SOCIEDAD COMERCIAL DE RESPONSABILIDAD LIMITADA  M2A SOLUTIONS S.R.L.",
            {"base": "M2A SOLUTIONS"},
        ),
    ),
)
def test_parse_duplicated_entries(name, expected):
    information = parse_name(name)
    print(information)
    for key, value in expected.items():
        assert set(getattr(information, key)) == set(value)


@pytest.mark.parametrize(
    ("name", "expected"),
    (
        ("and", {"base": ("and",)}),
        ("and and", {"base": ("and", "and")}),
        ("ve", {"base": ("ve",)}),
    ),
)
def test_problematic_strange_names(name, expected):
    information = parse_name(name)
    print(repr(information))
    for key, value in expected.items():
        assert len(getattr(information, key)) == len(value)
        assert set(getattr(information, key)) == set(value)
