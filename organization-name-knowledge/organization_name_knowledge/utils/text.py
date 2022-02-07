import re
from importlib.resources import open_text
from typing import List, Sequence, Tuple

import unidecode

from organization_name_knowledge import resources

with open_text(resources, "conjunctions.txt") as file:
    CONJUNCTIONS = {conjunction.lower() for conjunction in file.read().splitlines()}


with open_text(resources, "prepositions.txt") as file:
    PREPOSITIONS = {preposition.lower() for preposition in file.read().splitlines()}

SEPARATE_BY_CHARS = [
    ",",
    " -",
    "- ",
    "/",
    "\\",
    "+",
    "_",
    "=",
    "|",
    ":",
]

REMOVE_CHARS = ".()\"'`;!?@$#^~*%[]{}<>"
REMOVE_CHARS_REGEX = re.compile(rf"[{re.escape(REMOVE_CHARS)}]")

SPLIT_CHARS_REGEX = re.compile(r"|".join(rf"({re.escape(c)})" for c in SEPARATE_BY_CHARS))
SPLIT_AND_LEAVE_CHARS_REGEX = re.compile(r"((?<=\w{3})\.|\.(?=\w{3}))")

TOO_LONG_NUMBER_REGEX = re.compile(r"\d{4,}")


def alpha_char_count(text: str) -> int:
    return sum([1 for char in text if char.isalpha()])


def clear_name(name: str) -> str:
    name = unidecode.unidecode(name.lower()).strip()
    return REMOVE_CHARS_REGEX.sub("", name)


def contains_conjunction(tokens: Sequence[str]) -> bool:
    for conjunction in CONJUNCTIONS:
        if conjunction.lower() in tokens or conjunction.upper() in tokens:
            return True
    return False


def divide(name: str) -> Tuple[str, ...]:
    replaced = SPLIT_AND_LEAVE_CHARS_REGEX.sub(r"\1 ", SPLIT_CHARS_REGEX.sub(" ", name))
    return tuple(replaced.strip().split())


def is_dependent_token(text: str) -> bool:
    if text in CONJUNCTIONS or text in PREPOSITIONS:
        return True
    return False


def remove_split_chars(name: str) -> str:
    return SPLIT_AND_LEAVE_CHARS_REGEX.sub("", SPLIT_CHARS_REGEX.sub("", name))


def remove_too_long_numbers(text: str) -> str:
    return TOO_LONG_NUMBER_REGEX.sub("", text)


def split_text_by_too_long_numbers(text: str) -> List[str]:
    return [name.strip() for name in TOO_LONG_NUMBER_REGEX.split(text)]


def starts_with_conjunction(tokens: Sequence[str]) -> bool:
    """Returns true if any of conjunctions is the first of tokens (not a part of the first token)"""
    if tokens and tokens[0].lower() in CONJUNCTIONS:
        return True
    return False


def starts_with_preposition(tokens: Sequence[str]) -> bool:
    """Returns true if any of prepositions is the first of tokens (not a part of the first token)"""
    if tokens and tokens[0].lower() in PREPOSITIONS:
        return True
    return False
