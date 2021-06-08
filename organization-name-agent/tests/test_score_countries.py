import pytest

from company_name.compare import compare


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
    assert not scored['country'].value
