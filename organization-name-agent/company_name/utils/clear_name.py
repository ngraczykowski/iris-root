import re
from typing import Tuple

import unidecode

UNNECESSARY_CHARS_REGEX = re.compile(r",|( -)|(- )")
DOMAIN_REGEX = re.compile(r"\.\w{2,3}\b")  # .net, .com, .org, .de


def divide(name: str) -> Tuple[str]:
    return tuple(
        UNNECESSARY_CHARS_REGEX.sub(' ', name).strip().split()
    )


def clear_name(name: str) -> str:
    name = unidecode.unidecode(name.lower()).strip()
    return DOMAIN_REGEX.sub("", name)
