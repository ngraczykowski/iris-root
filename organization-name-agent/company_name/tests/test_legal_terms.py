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


@pytest.mark.parametrize(
    ("first", "second", "value"),
    (("A corp", "A corporation", 1), ("A corp", "A", 0.5), ("A plc", "A limited", 0)),
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
    assert result["legal_terms"].value == 0.5
    assert result["legal_terms"].compared
