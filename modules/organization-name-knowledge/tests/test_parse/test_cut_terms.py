import pytest

from organization_name_knowledge.knowledge_base.term_sources import TermSources
from organization_name_knowledge.names.parse.create_tokens import create_tokens
from organization_name_knowledge.names.parse.cut_terms import (
    _divide_name_for_possible_terms,
    cut_terms,
    cut_until_any_term_matches,
)

TERMS = TermSources(
    {
        ("mouse",),
        ("field", "mouse"),
        ("house", "mouse"),
        ("possum",),
        ("brushtail", "possum"),
        ("ringtail", "possum"),
        ("rat",),
        ("black", "rat"),
        ("brown", "rat"),
        ("wood", "rat"),
        ("cotton", "rat"),
        ("black", "and", "brown", "rat"),
        ("centipede",),
        ("house", "centipede"),
    }
)


@pytest.mark.parametrize(
    ("source", "expected"),
    (
        ("", ((), ())),
        ("house", (("house",), ())),
        ("mouse", ((), ("mouse",))),
        ("great mouse", (("great",), ("mouse",))),
        ("field mouse", ((), ("field mouse",))),
        ("great field mouse", (("great",), ("field mouse",))),
        ("mouse possum rat", ((), ("mouse", "possum", "rat"))),
        ("mouse brushtail possum rat", ((), ("mouse", "brushtail possum", "rat"))),
        ("horse mouse possum rat", (("horse",), ("mouse", "possum", "rat"))),
        ("mouse possum rat house", (("mouse", "possum", "rat", "house"), ())),
        ("mouse and rat", (("mouse", "and"), ("rat",))),
        ("black and brown rat", ((), ("black and brown rat",))),
    ),
)
def test_cut_terms(source, expected):
    assert cut_terms(create_tokens(source), TERMS) == expected


@pytest.mark.parametrize(
    ("source", "expected"),
    (
        ("", ((), ())),
        ("house", (("house",), ())),
        ("mouse", ((), ("mouse",))),
        ("field mouse", ((), ("field mouse",))),
        ("mouse possum rat", ((), ("mouse", "possum", "rat"))),
        ("horse mouse", (("horse", "mouse"), ())),
        ("mouse horse", (("horse",), ("mouse",))),
        ("mouse horse rat", (("horse", "rat"), ("mouse",))),
        ("mouse and rat", (("and", "rat"), ("mouse",))),
    ),
)
def test_cut_terms_from_start(source, expected):
    assert cut_terms(create_tokens(source), TERMS, from_start=True) == expected


@pytest.mark.parametrize(
    ("source", "expected"),
    (
        ("", ((), ())),
        ("house", (("house",), ())),
        ("mouse", ((), ("mouse",))),
        ("field mouse", ((), ("field mouse",))),
        ("mouse possum rat", ((), ("mouse", "possum", "rat"))),
        ("mouse and rat", ((), ("mouse", "and", "rat"))),
        ("and rat", ((), ("and", "rat"))),
        ("rat and", ((), ("rat", "and"))),
        ("black and brown rat", ((), ("black and brown rat",))),
    ),
)
def test_cut_terms_with_weak_words(source, expected):
    assert cut_terms(create_tokens(source), TERMS, with_weak_words=True) == expected


@pytest.mark.parametrize(
    ("source", "expected"),
    (
        ("", ((), ())),
        ("house", (("house",), ())),
        ("mouse", (("mouse",), ())),
        ("field mouse", (("field",), ("mouse",))),
        ("mouse possum rat", (("mouse",), ("possum", "rat"))),
        ("funny mouse possum", (("funny",), ("mouse", "possum"))),
        ("rat mouse possum", (("rat",), ("mouse", "possum"))),
    ),
)
def test_cut_terms_saving_at_least_one_word(source, expected):
    assert cut_terms(create_tokens(source), TERMS, saving_at_least_one_word=True) == expected


@pytest.mark.parametrize(
    ("source", "expected"),
    (
        ("", ((), ())),
        ("house", (("house",), ())),
        ("mouse", (("mouse",), ())),
        ("field mouse", (("field", "mouse"), ())),
        (
            "mouse possum rat",
            (
                ("rat",),
                (
                    "mouse",
                    "possum",
                ),
            ),
        ),
    ),
)
def test_cut_terms_from_start_saving_at_least_one_word(source, expected):
    assert (
        cut_terms(create_tokens(source), TERMS, from_start=True, saving_at_least_one_word=True)
        == expected
    )


@pytest.mark.parametrize(
    ("source", "expected"),
    (
        ("", ((), ())),
        ("house", (("house",), ())),
        ("mouse", ((), ("mouse",))),
        ("field mouse", ((), ("field mouse",))),
        ("mouse possum rat", ((), ("mouse", "possum", "rat"))),
        ("mouse and rat", ((), ("mouse", "and", "rat"))),
        (
            "and rat",
            (
                (),
                (
                    "and",
                    "rat",
                ),
            ),
        ),
        ("rat and", ((), ("rat", "and"))),
        ("black and brown rat", ((), ("black and brown rat",))),
    ),
)
def test_cut_terms_from_start_with_weak_words_between(source, expected):
    assert (
        cut_terms(create_tokens(source), TERMS, from_start=True, with_weak_words=True) == expected
    )


@pytest.mark.parametrize(
    ("source", "expected"),
    (
        ("", ((), ())),
        ("house", ((), ("house",))),
        ("mouse", (("mouse",), ())),
        ("mouse and", (("mouse",), ("and",))),
        ("mouse and aladeen", (("mouse",), ("and", "aladeen"))),
        ("very funny mouse catches cheese", (("very", "funny", "mouse"), ("catches", "cheese"))),
    ),
)
def test_cut_until_any_term_matches(source, expected):
    assert cut_until_any_term_matches(create_tokens(source), TERMS) == expected


@pytest.mark.parametrize(
    ("source", "expected"),
    (
        ("", ((), ())),
        ("house", ((), ("house",))),
        ("mouse", (("mouse",), ())),
        ("mouse and", (("mouse", "and"), ())),
        ("funny mouse met bug", (("mouse", "met", "bug"), ("funny",))),
        ("Go far far away rat stuart", (("rat", "stuart"), ("go", "far", "far", "away"))),
    ),
)
def test_cut_until_any_term_matched_from_start(source, expected):
    assert cut_until_any_term_matches(create_tokens(source), TERMS, from_start=True) == expected


@pytest.mark.parametrize(
    "name, max_length", [("Alpaca Bee Cheetah Deer Elephant Falcon Grizzly", 2)]
)
def test_divide_name_both_ways(name, max_length):
    name_tokens = create_tokens(name)
    # from start
    name_left, possible_term = next(
        _divide_name_for_possible_terms(name=name_tokens, max_length=max_length, from_start=True)
    )
    assert possible_term.original_name == "Alpaca Bee"

    # not from start
    name_left, possible_term = next(
        _divide_name_for_possible_terms(name=name_tokens, max_length=max_length, from_start=False)
    )
    assert possible_term.original_name == "Falcon Grizzly"
