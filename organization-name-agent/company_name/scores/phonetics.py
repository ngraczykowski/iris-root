import itertools
from typing import Tuple

import phonetics
import rapidfuzz.fuzz as fuzz

from company_name.names.name_information import TokensSequence
from company_name.scores.score import Score


def _compared(
    first_name: TokensSequence,
    second_name: TokensSequence,
    first_phonetic: str,
    second_phonetic: str,
) -> Tuple[Tuple[str, ...], Tuple[str, ...]]:
    return (
        (f"{first_phonetic} ({first_name.original_name})",),
        (f"{second_phonetic} ({second_name.original_name})",),
    )


def get_phonetic_score(
    first_name: TokensSequence, second_name: TokensSequence
) -> Score:
    scores = [
        (fuzz.ratio(n1, n2) / 100, (n1, n2))
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
    elif first_name and second_name:
        # some alphabets are not supported by library
        return Score(status=Score.ScoreStatus.NOT_APPLICABLE)
    else:
        return Score(compared=(first_name.original_tuple, second_name.original_tuple))
