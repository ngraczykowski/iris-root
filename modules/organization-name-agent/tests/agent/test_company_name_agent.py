import pytest

from company_name import CompanyNameAgent, Reason, Result, Solution


@pytest.mark.parametrize(
    ("first", "second"),
    (
        ((), ()),
        ((), ("COMPANY NAME",)),
        (("COMPANY NAME",), ()),
        (("",), ("",)),
        ((None,), (None,)),
        (("",), (None,)),
    ),
)
def test_when_no_names(first, second):
    print(first, second)
    result = CompanyNameAgent().resolve(first, second)
    print(result)
    assert result == Result(Solution.NO_DATA, Reason())


@pytest.mark.parametrize(
    ("first", "second", "expected_solution"),
    (
        (("Google limited liability company",), ("GOOGLE LLC",), Solution.MATCH),
        (("Google",), ("Facebook",), Solution.NO_MATCH),
    ),
)
def test_when_one_name(first, second, expected_solution):
    print(first, second)
    result = CompanyNameAgent().resolve(first, second)
    assert result.solution == expected_solution


@pytest.mark.parametrize(
    ("first", "second"),
    (
        (("GOOGLE LLC",), ("GOOGLE LLC",)),
        (("GOOGLE LLC",), ("GOOGLE LLC", "FACEBOOK", "AMAZON.COM")),
        (
            (
                "AGRICULTURAL BANK OF CHINA",
                "POWERCHINA",
            ),
            ("AGRICULTURAL BANK OF CHINA",),
        ),
    ),
)
def test_choose_exact_name(first, second):
    print(first, second)
    result = CompanyNameAgent().resolve(first, second)
    assert result.solution == Solution.MATCH
    assert (
        result.reason.results[0].alerted_party_name == result.reason.results[0].watchlist_party_name
    )


@pytest.mark.parametrize(("first", "second"), ((("THE VTB BANK",), ("SAFE NAME",)),))
def test_blacklist(first, second):
    print(first, second)
    assert CompanyNameAgent().resolve(first, second).solution == Solution.MATCH


@pytest.mark.parametrize(
    ("first", "second"),
    (
        (("THE NATIONAL COMMERCIAL BANK",), ("NCB",)),
        (
            ("THE NATIONAL COMMERCIAL BANK",),
            ("NCB", "BANK OF CHINA", "NATIONAL TREASURY"),
        ),
    ),
)
def test_abbreviation(first, second):
    print(first, second)
    result = CompanyNameAgent().resolve(first, second)
    assert result.solution == Solution.MATCH


@pytest.mark.parametrize(
    ("first", "second", "abbreviation_not_chosen"),
    (
        (
            ("THE NATIONAL COMMERCIAL BANK",),
            ("NCB", "THE NATIONAL COMMERCIAL BANK"),
            "NCB",
        ),
    ),
)
def test_choose_exact_name_over_abbreviation(first, second, abbreviation_not_chosen):
    print(first, second)
    result = CompanyNameAgent()._resolve(first, second)
    assert result.solution == Solution.MATCH
    assert abbreviation_not_chosen != result.reason.results[0].watchlist_party_name


@pytest.mark.parametrize(
    "first, second",
    [
        (("OSANG HEAL THCARE CO LTD INFOPIA",), ("INFOPIA CO LTD",)),
        (("SCCM DYBNG AND PRINTING CO. ATO",), ("ATO, OOO",)),
    ],
)
def test_match_when_base_name_after_legal_take_it_from_other(first, second):
    print(first, second)
    result = CompanyNameAgent().resolve(first, second)
    assert result.solution == Solution.MATCH
