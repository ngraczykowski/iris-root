import re
from typing import Sequence

import unidecode


def divide(name: str) -> Sequence[str]:
    divided = (
        name.replace(",", " ")
        .replace(" -", " ")
        .replace("- ", " ")
        .replace("  ", " ")
        .strip()
        .split()
    )
    return divided


def clear_name(name: str) -> str:
    name = unidecode.unidecode(name.lower()).strip()
    return re.sub(r"\.\w{2,3}\b", "", name)  # .net, .com, .org, .de
