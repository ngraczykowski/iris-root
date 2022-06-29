import itertools
import re
from itertools import combinations
from typing import List, Set

from organization_name_knowledge.utils.text import (
    CONJUNCTIONS,
    remove_split_chars,
    remove_too_long_numbers,
    split_text_by_too_long_numbers,
)

ORG_NAME_DELIMITERS = [",", ":", "+", "-", "/", "\n", "\r", "the "]
NUMERATORS = [str(num) + ")" for num in range(1, 9)] + [str(num) + "/" for num in range(1, 9)]
DELIMITERS = [" " + conj + " " for conj in CONJUNCTIONS] + ORG_NAME_DELIMITERS + NUMERATORS

PARENTHESES_REGEX = re.compile(r"\([^)]*\)")


def get_term_variants(term: str) -> Set[str]:
    def _single_word_variants(word: str):
        word_without_splits = remove_split_chars(word)
        return (
            word,
            word_without_splits,
            " ".join(word_without_splits),
        )

    return {
        term,
        *(
            " ".join(w).strip()
            for w in itertools.product(*[_single_word_variants(t) for t in term.split()])
        ),
    }


def get_text_variants(text: str) -> Set[str]:
    variants = set()
    # assuming single level instead of a recursive split
    _add_variants(text, DELIMITERS, variants)

    for variant in split_text_by_too_long_numbers(text):
        variants.add(variant)

    variants.add(text)
    _add_parentheses_based_variants(text, variants)
    return {remove_too_long_numbers(variant).strip() for variant in variants}


def _add_variants(name: str, delimiters: List[str], variants: Set[str]):
    for delimiter in delimiters:
        if delimiter in name:
            for name_variant in name.split(delimiter):
                variants.add(name_variant.strip())


def _add_parentheses_based_variants(text: str, variants: Set[str]):
    variants.add(text.replace("(", " ").replace(")", " "))
    variants.add(PARENTHESES_REGEX.sub("", text))


def get_substrings_from_consecutive_tokens(
    tokens: List[str], min_tokens_number: int, max_tokens_number: int
) -> List[str]:
    substrings = [
        " ".join(tokens[first:last])
        for first, last in combinations(range(len(tokens) + 1), 2)
        if min_tokens_number <= last - first <= max_tokens_number
    ]
    return substrings
