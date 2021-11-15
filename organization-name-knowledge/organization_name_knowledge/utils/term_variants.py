import itertools
import json
import re
from importlib.resources import open_text
from typing import Dict, List, Set

from organization_name_knowledge import resources
from organization_name_knowledge.utils.clear_name import remove_split_chars

NAMES_SYNONYMS: Dict[str, List[str]]

with open_text(resources, "eastern_male_names_mapping.json") as file:
    NAMES_SYNONYMS = json.load(file)
with open_text(resources, "eastern_female_names_mapping.json") as file:
    NAMES_SYNONYMS.update(json.load(file))


with open_text(resources, "name_delimiters.txt") as file:
    delimiter_words_from_file = [" " + word + " " for word in file.read().splitlines()]
    NAME_DELIMITERS: List[str] = delimiter_words_from_file + [",", ":", "+", "-", "/"]


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


def get_name_variants(name: str) -> List[str]:
    variants = set()
    # assuming single level instead of a recursive split
    for name_delimiter in NAME_DELIMITERS:
        for name_variant in name.split(name_delimiter):
            variants.add(name_variant.strip())
    for title in TITLES_FOR_VARIANTS:
        for name_variant in name.split(title):
            variants.add(name_variant.strip())
    return sorted(variants)
