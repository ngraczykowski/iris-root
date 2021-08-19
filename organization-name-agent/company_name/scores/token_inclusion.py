import itertools
from typing import Sequence

from company_name.names.name_information import NameInformation, Token, TokensSequence
from company_name.scores.score import Score
from company_name.utils.clear_name import POSSIBLE_SEPARATORS, clear_name


def _tokens(name: NameInformation) -> Sequence[TokensSequence]:
    return (
        name.name(),
        TokensSequence(
            list(
                itertools.chain.from_iterable(
                    [
                        Token(original=o, cleaned=clear_name(o))
                        for o in POSSIBLE_SEPARATORS.split(word)
                    ]
                    for word in name.name().original_tuple
                )
            )
        ),
    )


def _token_inclusion(name: TokensSequence, tokens: TokensSequence) -> Score:
    return Score(
        value=float(len(name) == 1 and name[0] in tokens),
        compared=(name.original_tuple, tokens.original_tuple),
    )


def get_token_inclusion_score(first: NameInformation, second: NameInformation) -> Score:
    names = first.name(), second.name()
    if not all(names):
        return Score(compared=(names[0].original_tuple, names[1].original_tuple))

    words_length = sorted(len(name.original_tuple) for name in names)
    if words_length[0] != 1 or words_length[1] == 1:
        return Score(status=Score.ScoreStatus.NOT_APPLICABLE)

    return max(
        *(_token_inclusion(first.name(), tokens) for tokens in _tokens(second)),
        *(
            reversed(_token_inclusion(second.name(), tokens))
            for tokens in _tokens(first)
        )
    )
