from typing import Sequence

from company_name.names.name_information import NameInformation
from company_name.scores.score import Score
from company_name.utils.clear_name import divide, clear_name


def _token_inclusion(first: Sequence[str], second: Sequence[str]) -> bool:
    return len(first) == 1 and len(second) > 1 and first[0] in second


def token_inclusion_score(first: NameInformation, second: NameInformation) -> Score:
    original_words = divide(first.original.original), divide(second.original.original)
    cleaned_words = [[clear_name(w) for w in word] for word in original_words]
    return Score(
        value=float(
            _token_inclusion(*cleaned_words)
            or _token_inclusion(*reversed(cleaned_words))
        ),
        compared=(original_words[0], original_words[1]),
    )
