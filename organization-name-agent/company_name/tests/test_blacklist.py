import pytest

from company_name.compare import compare


@pytest.mark.parametrize(
    ("first", "second"),
    (
        ("International School of Currency", "middle east fz company"),
        ("Amazing company", "KAPITAL ZHILAYA NEDVIZHIMOST OOO"),
    )
)
def test_no_blacklist(first, second):
    print(repr(first), repr(second))
    score = compare(first, second)
    print(score)
    assert score['blacklisted'].value == 0
    assert score['blacklisted'].compared == ((), ())

@pytest.mark.parametrize(
    ("first", "second", "blacklisted"),
    (
        ("Gazprom International School of Currency", "amazing company", (("gazprom", ), ())),
        ("amazing company",  "VTB KAPITAL ZHILAYA NEDVIZHIMOST OOO", ((), ("vtb", ))),
        ("VTB-company", "company-something_very-vtb-serious-gazprom", (("vtb", ), ("vtb", "gazprom")))
    )
)
def test_simple_blacklist(first, second, blacklisted):
    print(repr(first), repr(second))
    score = compare(first, second)
    print(score)
    assert score['blacklisted'].value == 1
    assert score['blacklisted'].compared == blacklisted


@pytest.mark.parametrize(
    ("first", "second"),
    (
        ("amazing company", "company-rtsadvtbafsadsad-ooo"),
    )
)
def test_blacklist_as_part_of_other_word(first, second):
    print(repr(first), repr(second))
    score = compare(first, second)
    print(score)
    assert score['blacklisted'].value == 0
    assert score['blacklisted'].compared == ((), ())
