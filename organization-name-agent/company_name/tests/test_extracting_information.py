import pytest

from ..compare import compare


@pytest.mark.parametrize(
    ("name", "expected_legal_term"),
    (
        ("Google", ()),
        ("EISOO Information Technology Corp.", ("corp.",)),
        ("Atlassian Corporation Plc", ("corporation", "plc")),
        ("RENDEZ Rail CZ s. r. o.", ("s. r. o.", )),
        ("Nejdecká česárna vlny, a. s.", ("a. s.", )),
        ("AIRSTAL SP Z O O", ("sp z o o", )),
        ("AGB Señalización y Publicidad, S.A. de C.V.", ("s.a.", "de c.v.")),
        ("SIGNCRAFT PTY. LIMITED", ("pty.", "limited")),
        ("C S Central America Sociedad Anonima de Capital Variable", ("sociedad anonima", "de capital variable")),
        ("DIM TU TAC TRADING SERVICE JOINT STOCK COMPANY", ("joint stock company", )),
        ("Ster - Planungs- u. Bau Gesellschaft m.b.H.", ("gesellschaft m.b.h.", )),
        ("Agentur Effect Ges.m.b.H.", ("ges.m.b.h.", )),
        ("ML ABUNDANCE PTE. LTD.", ("pte.", "ltd.")),
        ("YOU CONSULT e.U.", ("e.u.", )),
        ("Edelmann Hungary Packaging Zártkörűen Működő Részvénytársaság", ('zartkoruen mukodo reszvenytarsasag',))
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


@pytest.mark.parametrize(
    ("name", "expected"),
    (
            ("THOMAS J. COLEMAN AND COMPANY LIMITED", {"base": ("thomas", "j.", "coleman", "and", "company"), "legal": ("limited", )}),
            ("MS DESIGN & CONSTRUCTIONS", {"base": ("ms", "design", "&", "constructions")}),
            ("group and AAAAAAAA", {"common_prefixes": (), "base": ("group", "and", "aaaaaaaa")})
    )
)
def test_do_not_divide_when_and(name, expected):
    _, (information, _) = compare(name, "AAAA")
    print(information)
    for key, value in expected.items():
        assert set(information[key]) == set(value)


@pytest.mark.parametrize(
    ("name", "expected"),
    (
            ("TONTINE ROOMS HOLDING COMPANY LIMITED - THE", {"legal": ("company", "limited")}),
    )
)
def test_the_on_the_end_do_not_destroy_cutting_other_information(name, expected):
    _, (information, _) = compare(name, "AAAA")
    print(information)
    for key, value in expected.items():
        assert set(information[key]) == set(value)
