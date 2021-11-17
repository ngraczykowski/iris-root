import re
from typing import List, Tuple

import unidecode

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

REMOVE_CHARS_REGEX = re.compile(fr"[{re.escape(REMOVE_CHARS)}]")
SPLIT_CHARS_REGEX = re.compile(r"|".join(fr"({re.escape(c)})" for c in SEPARATE_BY_CHARS))
SPLIT_AND_LEAVE_CHARS_REGEX = re.compile(r"((?<=\w{3})\.|\.(?=\w{3}))")
TOO_LONG_NUMBER_REGEX = re.compile(r"\d{4,}")


def remove_split_chars(name: str) -> str:
    return SPLIT_AND_LEAVE_CHARS_REGEX.sub("", SPLIT_CHARS_REGEX.sub("", name))


def divide(name: str) -> Tuple[str, ...]:
    replaced = SPLIT_AND_LEAVE_CHARS_REGEX.sub(r"\1 ", SPLIT_CHARS_REGEX.sub(" ", name))
    return tuple(replaced.strip().split())


def clear_name(name: str) -> str:
    name = unidecode.unidecode(name.lower()).strip()
    return REMOVE_CHARS_REGEX.sub("", name)


def split_text_by_too_long_numbers(text: str) -> List[str]:
    return [name.strip() for name in TOO_LONG_NUMBER_REGEX.split(text)]


def remove_too_long_numbers(text: str) -> str:
    return TOO_LONG_NUMBER_REGEX.sub("", text)
