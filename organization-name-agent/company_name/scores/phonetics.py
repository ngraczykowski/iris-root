import itertools
from typing import Tuple

import fuzzywuzzy.fuzz
import phonetics

from company_name.names.name_information import NameSequence
from .score import Score


def _compared(
    first_name: NameSequence,
    second_name: NameSequence,
    first_phonetic: str,
    second_phonetic: str,
) -> Tuple[Tuple[str, ...], Tuple[str, ...]]:
    return (
        (f"{first_phonetic} ({first_name})",),
        (f"{second_phonetic} ({second_name})",)
    )


def phonetic_score(first_name: NameSequence, second_name: NameSequence) -> Score:
    scores = [
        (fuzzywuzzy.fuzz.ratio(n1, n2) / 100, (n1, n2))
        for n1, n2 in itertools.product(
            phonetics.dmetaphone(first_name.original_name),
            phonetics.dmetaphone(second_name.original_name),
        )
        if n1 and n2
    ]
    if scores:
        max_score = max(scores)
        return Score(
            value=max_score[0],
            compared=_compared(first_name, second_name, *max_score[1]),
        )
    else:
        return Score(value=0, compared=((), ()))
