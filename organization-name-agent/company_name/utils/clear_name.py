import re
from typing import Tuple

import unidecode

POSSIBLE_SEPARATORS = re.compile(r"-|_|—")
REMOVE_CHARS_REGEX = re.compile(r"\.|\"|'")
UNNECESSARY_CHARS_REGEX = re.compile(r",|( -)|(- )")
DOMAIN_REGEX = re.compile(r"\.\w{2,3}\b")  # .net, .com, .org, .de


def divide(name: str) -> Tuple[str, ...]:
    return tuple(UNNECESSARY_CHARS_REGEX.sub(' ', name).strip().split())


def clear_name(name: str) -> str:
    name = unidecode.unidecode(name.lower()).strip()
    return REMOVE_CHARS_REGEX.sub('', DOMAIN_REGEX.sub("", name))
