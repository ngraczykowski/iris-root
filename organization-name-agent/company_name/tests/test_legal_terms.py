import contextlib
import json
import pathlib
import tempfile

import pytest

from company_name.names.legal_terms import LegalTerms
from company_name.compare import compare


@contextlib.contextmanager
def legal_terms(data):
    with tempfile.TemporaryDirectory() as tempdir:
        path = pathlib.Path(tempdir) / "test.json"
        with open(path, "wt", encoding="utf-8") as f:
            json.dump(data, f)

        yield LegalTerms(source_path=path)


@pytest.mark.parametrize(
    ("source", "result"),
    (
        ("company", ("company",)),
        ("limited liability company", ("limited", "liability", "company")),
    ),
)
def test_one_legal_term(source, result):
    with legal_terms({source: []}) as terms:
        assert terms.all_legal_terms == {result}
        assert terms.legal_terms_mapping == {result: [result]}


@pytest.mark.parametrize(
    ("main_name", "aliases", "results"),
    (
        ("company", ("co",), {("company",), ("co",)}),
        (
            "limited liability company",
            ("limited", "llc"),
            {("limited", "liability", "company"), ("limited",), ("llc",)},
        ),
    ),
)
def test_legal_aliases(main_name, aliases, results):
    with legal_terms({main_name: aliases}) as terms:
        assert set(results).issubset(terms.all_legal_terms)

        identifier = set(terms.legal_terms_mapping[r][0] for r in results)
        assert len(identifier) == 1
        assert identifier.issubset(results)


def test_basic_legal_terms():
    terms = LegalTerms()
    assert ("corporation",) in terms.all_legal_terms
    assert ("corp",) in terms.all_legal_terms
    assert ("corp.",) in terms.all_legal_terms
    assert terms.legal_terms_mapping[("corp",)] == terms.legal_terms_mapping[("corp.",)]


def _get_legal_terms_result(first_legal: str, second_legal: str) -> float:
    result = compare("AAAA " + first_legal, "AAAA " + second_legal)
    print(result)
    assert " ".join(result["legal_terms"].compared[0]) == first_legal.replace(",", "")
    assert " ".join(result["legal_terms"].compared[1]) == second_legal.replace(",", "")
    return result["legal_terms"].value


@pytest.mark.parametrize(
    ("first", "second", "value"),
    (("A corp", "A corporation", 1), ("A corp", "A", 0), ("A plc", "A limited", 0), ("A", "A", None)),
)
def test_compare_legal_terms(first, second, value):
    print(repr(first), repr(second), compare(first, second))
    assert compare(first, second)["legal_terms"].value == value


@pytest.mark.parametrize(
    ("first", "second"),
    (
        ("ASML Holding", "ASML Holding N.V."),
        ("CDW Corporation", "CDW"),
        ("Charter Communications, Inc.", "Charter"),
        ("toyota motor finance", "TOYOTA MOTOR FINANCE (CHINA) COMPANY LIMITED"),
    ),
)
def test_compare_legal_on_one_side(first, second):
    print(repr(first), repr(second), compare(first, second))
    result = compare(first, second)
    assert result["fuzzy_on_base"].value == 1
    assert result["legal_terms"].value == 0
    assert result["legal_terms"].compared


@pytest.mark.parametrize(
    ("first", "second"),
    (
        ("AIRSTAL SP Z O O", "AIRSTAL sp. z o.o."),
        ("Nejdecká česárna vlny, a. s.", "Nejdecká česárna vlny AS"),
        ("BULLDOG SPORTS S A S", "BULLDOG SPORTS SAS"),
        ("COMPLEXICA PTY LTD", "COMPLEXICA PTY. LTD."),
        ("FLORAL MANUFACTURING GROUP COMPANY LIMITED", "FLORAL MANUFACTURING GROUP COMPANY LTD."),
        ("AGENSI PEKERJAAN PERTAMA SENDIRIAN BERHAD", "AGENSI PEKERJAAN PERTAMA SDN. BHD."),
    ),
)
def test_compare_same_legal_terms(first, second):
    result = compare(first, second)
    assert result["fuzzy"].value == 1
    assert result["legal_terms"].value == 1


@pytest.mark.parametrize(
    ("first_legal", "second_legal"),
    (
        ("GmbH & Co KG", "GmbH"),
    ),
)
def test_partial_legal_terms(first_legal, second_legal):
    assert _get_legal_terms_result(first_legal, second_legal) > 0


@pytest.mark.parametrize(
    ("first_legal", "second_legal"),
    (
        ("Co., Ltd.", "Ltd Company"),
    ),
)
def test_compare_multiple_legal_terms(first_legal, second_legal):
    assert _get_legal_terms_result(first_legal, second_legal) == 1