import pytest

from company_name.compare import compare, parse_name


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
        ("(U.K.) Google", ("u.k.",)),
    ),
)
def test_parse_country(name, expected_country):
    information = parse_name(name)
    assert set(information.countries) == set(expected_country)


@pytest.mark.parametrize(
    ("first", "first_country", "second", "second_country"),
    (
        (
            "TOYOTA MOTOR FINANCE (CHINA) COMPANY LIMITED",
            "CHINA",
            "TOYOTA MOTOR FINANCE (THE PEOPLE'S REPUBLIC OF CHINA) COMPANY LIMITED",
            "THE PEOPLE'S REPUBLIC OF CHINA",
        ),
        ("Nissan (UK) ", "UK", "Somophore (United Kingdom)", "United Kingdom"),
        ("Nissan (UK) France", "UK", "Nissan (U.K.) Niger", "U.K."),
    ),
)
def test_matching_countries(first, first_country, second, second_country):
    print(repr(first), repr(second))
    scored = compare(first, second)
    print(scored)
    assert scored['country'].value == 1
    assert scored['country'].compared == ((first_country,), (second_country,))


@pytest.mark.parametrize(
    ("first", "first_country", "second", "second_country"),
    (("Nissan (UK) Limited", "UK", "Nissan (CHINA) Limited", "CHINA"),),
)
def test_different_countries(first, first_country, second, second_country):
    print(repr(first), repr(second))
    scored = compare(first, second)
    print(scored)
    assert scored['country'].value == 0
    assert scored['country'].compared == ((first_country,), (second_country,))


@pytest.mark.parametrize(
    ("first", "second"),
    (
        ("Nissan (UK) Limited", "Nissan"),
        ("Nissan (awesome) Limited", "Nissan (awesome) corporation"),
    ),
)
def test_no_countries(first, second):
    print(repr(first), repr(second))
    scored = compare(first, second)
    print(scored)
    assert scored['country'].value == 0.5
