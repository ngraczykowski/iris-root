import json
from typing import List

import pytest

from organization_name_knowledge.knowledge_base.legal_terms import LegalTerms
from organization_name_knowledge.names.parse.parse import parse_name


def test_basic_legal_terms():
    with open("./organization_name_knowledge/resources/legal_terms.json") as f:
        terms = LegalTerms(json.load(f))
    assert ("corporation",) in terms.legal_term_sources
    assert ("corp",) in terms.legal_term_sources
    assert ("corp.",) not in terms.legal_term_sources
    assert terms.source_to_legal_terms[("corp",)] == terms.source_to_legal_terms[("corporation",)]


@pytest.mark.parametrize(
    ("name", "expected_legal_term"),
    (
        ("Google", ()),
        ("EISOO Information Technology Corp.", ("corp",)),
        ("Atlassian Corporation Plc", ("corporation", "plc")),
        ("RENDEZ Rail CZ s. r. o.", ("s r o",)),
        ("Nejdecká česárna vlny, a. s.", ("a s",)),
        ("AIRSTAL SP Z O O", ("sp z o o",)),
        ("AGB Señalización y Publicidad, S.A. de C.V.", ("sa", "de cv")),
        ("SIGNCRAFT PTY. LIMITED", ("pty", "limited")),
        (
            "C S Central America Sociedad Anonima de Capital Variable",
            ("sociedad anonima", "de capital variable"),
        ),
        ("DIM TU TAC TRADING SERVICE JOINT STOCK COMPANY", ("joint stock company",)),
        ("Ster - Planungs- u. Bau Gesellschaft m.b.H.", ("gesellschaft mbh",)),
        ("Agentur Effect Ges.m.b.H.", ("ges mbh",)),
        ("ML ABUNDANCE PTE. LTD.", ("pte", "ltd")),
        ("YOU CONSULT e.U.", ("eu",)),
        (
            "Edelmann Hungary Packaging Zártkörűen Működő Részvénytársaság",
            ("zartkoruen mukodo reszvenytarsasag",),
        ),
    ),
)
def test_legal_term(name, expected_legal_term):
    information = parse_name(name)
    assert information.legal.cleaned_tuple == expected_legal_term


@pytest.mark.parametrize(
    ("name", "expected"),
    (
        (
            "THE LARSEN & TOUBRO LIMITED PROVIDENT FUND OF 1952",
            {
                "legal": ("limited",),
                "other": (
                    "provident",
                    "fund",
                    "of",
                    "1952",
                ),
            },
        ),
        (
            "CHINA ELECTRONICS IMPORT & EXPORT CORPORATION TIANJIN",
            {"legal": ("corporation",)},
        ),
        ("LUBELSKIE BIOGAZOWNIE SP Z O O W RESTRUKTURYZACJ", {"legal": ("sp z o o",)}),
    ),
)
def test_words_after_legal_terms_are_ignored(name, expected):
    information = parse_name(name)
    print(information)
    for key, value in expected.items():
        assert set(getattr(information, key)) == set(value)


@pytest.mark.parametrize(
    ("name", "legal"),
    (
        (
            "U-G-T Group LLC(Limited liability Company)",
            {"group", "llc", "limited liability company"},
        ),
        ("SIGNATURE COSMETICS (PTY) LTD ", {"pty", "ltd"}),
        ("Pax Holding (Genossenschaft) ", {"genossenschaft"}),
        ("OMEN CASTING (LIMITED PARTNERSHIP)", {"limited partnership"}),
    ),
)
def test_legal_in_parenthesis(name: str, legal: str):
    information = parse_name(name)
    print(information)
    assert set(information.legal) == legal


@pytest.mark.parametrize(
    ("name", "expected"),
    (("TADIRAN TELECOM (TTL), LIMITED PRTNERSHIP", {"base": ("tadiran", "telecom")}),),
)
def test_legal_with_typos(name: str, expected):
    information = parse_name(name)
    print(information)
    for key, value in expected.items():
        assert getattr(information, key) == value


@pytest.mark.parametrize(
    "name",
    ("POWER SAL", "POWER S A L", "POWER S.A.L.", "POWER (SAL)", "POWER (S.A.L.)"),
)
def test_different_ways_to_use_legal_term_abbreviation(name):
    information = parse_name(name)
    print(information)
    assert len(information.legal) == 1


@pytest.mark.parametrize(
    "legal_term",
    ("sp zoo", "sp. z o.o", "sp. z o.o.", "sp. zoo", "sp z oo", "sp z o o"),
)
def test_legal_terms_in_different_format(legal_term: str):
    information = parse_name(f"google {legal_term}")
    assert information.base == ["google"]
    assert len(information.legal) == 1


@pytest.mark.parametrize(
    ("name", "expected_legals"),
    (
        ("ECO1.Corp.", ["corp"]),
        ("SCCM DYBNG AND PRINTING CO.ATO", ["co"]),
    ),
)
def test_parse_legals_with_dots(name: str, expected_legals: List[str]):
    information = parse_name(name)
    assert information.legal == expected_legals
