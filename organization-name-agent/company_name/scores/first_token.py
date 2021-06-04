from typing import Optional, Tuple

from company_name.names.name_information import NameInformation
from company_name.scores.score import Score


def _compared(
    first: Optional[str], second: Optional[str]
) -> Tuple[Tuple[str, ...], Tuple[str, ...]]:
    return (first,) if first else (), (second,) if second else ()


def first_token_score(first: NameInformation, second: NameInformation) -> Score:
    first_tokens = (
        first.base[0] if first.base else None,
        second.base[0] if second.base else None,
    )
    if not all(first_tokens):
        return Score(value=0, compared=_compared(*first_tokens))

    return Score(
        value=float(first_tokens[0] == first_tokens[1]),
        compared=_compared(*first_tokens),
    )
