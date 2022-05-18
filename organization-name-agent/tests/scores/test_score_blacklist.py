import pytest
from organization_name_knowledge import parse

from company_name.agent.agent import CompanyNameAgent


@pytest.mark.parametrize(
    ("first", "second"),
    (
        ("International School of Currency", "middle east fz company"),
        ("Amazing company", "KAPITAL ZHILAYA NEDVIZHIMOST OOO"),
    ),
)
def test_no_blacklist(first, second):
    print(repr(first), repr(second))
    score = CompanyNameAgent().blacklist.get_blacklist_score(parse(first), parse(second))
    print(score)
    assert score.status.name == "OK"
    assert score.value == 0
    assert score.compared == ((), ())


@pytest.mark.parametrize(
    ("first", "second", "blacklisted"),
    (
        (
            "Gazprom International School of Currency",
            "amazing company",
            (("gazprom",), ()),
        ),
        ("VTB KAPITAL ZHILAYA NEDVIZHIMOST OOO", "amazing company", (("vtb",), ())),
        (
            "company-something_very-vtb-serious-gazprom",
            "VTB-kapital",
            (("vtb", "gazprom"), ()),
        ),
    ),
)
def test_simple_blacklist(first, second, blacklisted):
    print(repr(first), repr(second))
    score = CompanyNameAgent().blacklist.get_blacklist_score(parse(first), parse(second))
    print(score)

    assert score.status.name == "OK"
    assert score.value == 1
    assert score.compared == blacklisted


@pytest.mark.parametrize(
    "first, second, score_value, blacklisted",
    [
        ("ABCvtbDEF", "another company", 1.0, (("vtb",), ())),
        ("Russian-Gazprom-Federation", "Nord Stream", 1.0, (("gazprom",), ())),
        ("amazing company", "company-rtsadvtbafsadsad-ooo", 0.0, ((), ())),  # in wl - not find
    ],
)
def test_blacklist_as_part_of_other_word(first, second, score_value, blacklisted):
    print(repr(first), repr(second))
    score = CompanyNameAgent().blacklist.get_blacklist_score(parse(first), parse(second))
    print(score)

    assert score.status.name == "OK"
    assert score.value == score_value
    assert score.compared == blacklisted


@pytest.mark.parametrize(
    ("first", "second"),
    (
        ("amazing company", "Gazprom International School of Currency"),
        ("amazing company", "company-something_very-vtb-serious-gazprom"),
    ),
)
def test_no_blacklist_on_watchlist_name(first, second):
    print(repr(first), repr(second))
    score = CompanyNameAgent().blacklist.get_blacklist_score(parse(first), parse(second))
    print(score)

    assert score.status.name == "OK"
    assert score.value == 0
    assert score.compared == ((), ())
