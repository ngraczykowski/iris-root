from typing import Optional, Tuple

from organization_name_knowledge.names.name_information import NameInformation
from organization_name_knowledge.names.token import Token

from company_name.scores.score import Score


def _compared(
    first: Optional[Token], second: Optional[Token]
) -> Tuple[Tuple[str, ...], Tuple[str, ...]]:
    return (first.original,) if first else (), (second.original,) if second else ()


def get_first_token_score(first: NameInformation, second: NameInformation) -> Score:
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
