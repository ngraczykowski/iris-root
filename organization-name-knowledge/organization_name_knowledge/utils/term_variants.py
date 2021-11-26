import itertools
import json
import re
from importlib.resources import open_text
from typing import Dict, List, Set

from organization_name_knowledge import resources
from organization_name_knowledge.utils.text import (
    remove_split_chars,
    remove_too_long_numbers,
    split_text_by_too_long_numbers,
)

NAMES_SYNONYMS: Dict[str, List[str]]

with open_text(resources, "eastern_male_names_mapping.json") as file:
    NAMES_SYNONYMS = json.load(file)
with open_text(resources, "eastern_female_names_mapping.json") as file:
    NAMES_SYNONYMS.update(json.load(file))


with open_text(resources, "conjunctions.txt") as file:
    delimiter_words_from_file = [" " + word + " " for word in file.read().splitlines()]
    NAME_DELIMITERS: List[str] = delimiter_words_from_file + [",", ":", "+", "-", "/", "\n", "\r"]


with open_text(resources, "titles.txt") as file:
    lines: List[str] = file.read().splitlines()
    TITLES_REGEXES: List[re.Pattern] = [re.compile(f"[^a-z]{title}\\s") for title in lines]

    # to avoid i.e. "med" splitting "Mohammed XY"
    TITLES_FOR_VARIANTS: List[str] = [f" {title} " for title in lines]


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
    _add_variants(text, NAME_DELIMITERS, variants)
    _add_variants(text, TITLES_FOR_VARIANTS, variants)

    for variant in split_text_by_too_long_numbers(text):
        variants.add(variant)
    variants.add(text)
    return {remove_too_long_numbers(variant).strip() for variant in variants}


def _add_variants(name: str, delimiters: List[str], variants: Set[str]):
    for delimiter in delimiters:
        if delimiter in name:
            for name_variant in name.split(delimiter):
                variants.add(name_variant.strip())
