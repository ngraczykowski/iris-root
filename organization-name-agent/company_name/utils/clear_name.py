import re
from typing import Tuple

import unidecode

POSSIBLE_SEPARATORS = re.compile(r"[-—]")

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
LEAVE_CHARS = "&-—"

REMOVE_CHARS_REGEX = re.compile(fr"[{re.escape(REMOVE_CHARS)}]")
SPLIT_CHARS_REGEX = re.compile(r"|".join(fr"({re.escape(c)})" for c in SEPARATE_BY_CHARS))
SPLIT_AND_LEAVE_CHARS_REGEX = re.compile(r"((?<=\w{3})\.|\.(?=\w{3}))")


def remove_split_chars(name: str) -> str:
    return SPLIT_AND_LEAVE_CHARS_REGEX.sub("", SPLIT_CHARS_REGEX.sub("", name))


def divide(name: str) -> Tuple[str, ...]:
    replaced = SPLIT_AND_LEAVE_CHARS_REGEX.sub(r"\1 ", SPLIT_CHARS_REGEX.sub(" ", name))
    return tuple(replaced.strip().split())


def clear_name(name: str) -> str:
    name = unidecode.unidecode(name.lower()).strip()
    return REMOVE_CHARS_REGEX.sub("", name)
