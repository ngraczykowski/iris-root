import dataclasses

import pytest

from company_name import Score, compare


@dataclasses.dataclass
class EstimatedValue(float):
    lower: float = 0
    upper: float = 1

    def __eq__(self, value):
        return self.lower <= value <= self.upper


class AnyValue:
    def __eq__(self, value):
        return True


ANY_COMPARED = (AnyValue(), AnyValue())


@pytest.mark.parametrize(
    ("first", "second", "expected"),
    (
        (
            "TAKAFUL INSURANCE CO",
            "TAKAFUL",
            {
                "token_inclusion": Score(
                    value=1, compared=(("TAKAFUL", "INSURANCE"), ("TAKAFUL",))
                ),
                "fuzzy_on_base": Score(value=1, compared=(("TAKAFUL",), ("TAKAFUL",))),
            },
        ),
        (
            "HP, INC",
            "HEWLETT-PACKARD COMPANY (HP CO.)",
            {
                "abbreviation": Score(value=1, compared=(("HP",), ("HEWLETT-PACKARD",))),
                "legal_terms": Score(value=0, compared=(("INC",), ("COMPANY",))),
                "parenthesis_match": Score(value=1, compared=ANY_COMPARED),
            },
        ),
        (
            "SUMITOMO CORPORATION",
            "SUMITOMO SHOJI KAISHA, LTD",
            {
                "token_inclusion": Score(
                    value=1, compared=(("SUMITOMO",), ("SUMITOMO", "SHOJI", "KAISHA"))
                ),
                "legal_terms": Score(value=0, compared=(("CORPORATION",), ("LTD",))),
            },
        ),
        (
            "THE LARSEN & TOUBRO LIMITED PROVIDENT FUND OF 1952",
            "LARSEN AND TOUBRO LIMITED",
            {
                "token_inclusion": Score(status=Score.ScoreStatus.NOT_APPLICABLE, value=0),
                "legal_terms": Score(value=1, compared=(("LIMITED",), ("LIMITED",))),
                "fuzzy_on_base": Score(
                    value=EstimatedValue(lower=0.8),
                    compared=(("LARSEN & TOUBRO",), ("LARSEN AND TOUBRO",)),
                ),
            },
        ),
        (
            "THE EMBASSY OF THE REPUBLIC OF ANGOLA IN THE REPUBLIC OF KENYA",
            "EMBASSY OF THE REPUBLIC OF ANGOLA",
            {
                "token_inclusion": Score(status=Score.ScoreStatus.NOT_APPLICABLE, value=0),
                "legal_terms": Score(),
                "fuzzy": Score(value=EstimatedValue(lower=0.7), compared=ANY_COMPARED),
                "fuzzy_on_base": Score(value=EstimatedValue(lower=0.7), compared=ANY_COMPARED),
            },
        ),
        (
            "GRAND PARTNER LIMITED",
            "GROUP GRAND LTD",
            {"potential_subsidiary": Score(value=1, compared=((), ("GROUP GRAND LTD",)))},
        ),
        (
            "CHINA NATIONAL ELECTRONICS IMP & EXP CORPORATION",
            "CHINA ELECTRONICS IMPORT & EXPORT CORPORATION TIANJIN",
            {
                "abbreviation": Score(value=0, compared=ANY_COMPARED),
                "fuzzy_on_base": Score(
                    value=EstimatedValue(lower=0.7),
                    compared=(("CHINA NATIONAL ELECTRONICS",), ("CHINA ELECTRONICS",)),
                ),
                "fuzzy_on_suffix": Score(
                    value=EstimatedValue(lower=0.7),
                    compared=(("IMP & EXP",), ("IMPORT & EXPORT",)),
                ),
                "fuzzy": Score(
                    value=EstimatedValue(lower=0.7),
                    compared=(
                        ("CHINA NATIONAL ELECTRONICS IMP & EXP",),
                        ("CHINA ELECTRONICS IMPORT & EXPORT",),
                    ),
                ),
            },
        ),
        (
            "Deutsche Asset Mgt (Asia)",
            "DEUTSCHE ASSET MANAGEMENT",
            {
                "fuzzy": Score(
                    value=EstimatedValue(lower=0.8),
                    compared=(("Deutsche Asset Mgt",), ("DEUTSCHE ASSET MANAGEMENT",)),
                ),
                "country": Score(status=Score.ScoreStatus.NO_DATA),
            },
        ),
        (
            "AL JAZEIRA SERVICES SOA (HFSM)",
            "AL JAZERRA",
            {
                "fuzzy_on_base": Score(
                    value=EstimatedValue(lower=0.8),
                    compared=(("AL JAZEIRA",), ("AL JAZERRA",)),
                )
            },
        ),
        (
            "NORTH CHINA INDUSTRIES GROUP CORPORATION",
            "China North Industries Group Corp Ltd",
            {
                "tokenization": Score(
                    value=1,
                    compared=(
                        ("NORTH", "CHINA", "INDUSTRIES"),
                        ("China", "North", "Industries"),
                    ),
                ),
                "absolute_tokenization": Score(
                    value=3,
                    compared=(
                        ("NORTH", "CHINA", "INDUSTRIES"),
                        ("China", "North", "Industries"),
                    ),
                ),
                "legal_terms": Score(
                    value=EstimatedValue(lower=0.8, upper=0.9),
                    compared=(
                        (
                            "GROUP",
                            "CORPORATION",
                        ),
                        ("Group", "Corp", "Ltd"),
                    ),
                ),
            },
        ),
        (
            "The Korea Development bank, Seocho Branch",
            "THE KOREA DEVELOPMENT BANK (KDB)",
            {
                "fuzzy": Score(value=EstimatedValue(lower=0.75), compared=ANY_COMPARED),
            },
        ),
        (
            "Alpha Engineering Co Private Ltd.",
            "Charlie Engineering Co Private Ltd.",
            {
                "fuzzy_on_base": Score(
                    value=EstimatedValue(upper=0.4), compared=(("Alpha",), ("Charlie",))
                ),
                "fuzzy_on_suffix": Score(value=1, compared=(("Engineering",), ("Engineering",))),
            },
        ),
        (
            "Remit OOO",
            "Out Of Order Advertisements",
            {
                "legal_terms": Score(
                    status=Score.ScoreStatus.NO_MATCHED_PARTY_DATA,
                    compared=(("OOO",), ()),
                ),
            },
        ),
        (
            "Seventh Avenue, Inc.",
            "7th Avenue",
            {
                "fuzzy_on_base": Score(value=1, compared=(("Seventh Avenue",), ("7th Avenue",))),
                "fuzzy": Score(value=1, compared=(("Seventh Avenue",), ("7th Avenue",))),
            },
        ),
    ),
)
def test_known_cases(first, second, expected):
    result = compare(first, second)
    print(result, expected)
    for key, value in expected.items():
        assert result[key] == value
