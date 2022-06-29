import itertools
from typing import List, Sequence

from organization_name_knowledge import NameInformation, Token, TokensSequence

from company_name.scores.score import Score
from company_name.utils.clear_name import POSSIBLE_SEPARATORS, clear_name
from company_name.utils.generate_subsets import generate_words_subsets


def _tokens(name: NameInformation) -> Sequence[TokensSequence]:
    return (
        name.combined_name(),
        get_name_and_other_tokens(name, separated=True),
        get_name_and_other_tokens(name, separated=False),
    )


def get_name_and_other_tokens(name: NameInformation, separated: bool) -> TokensSequence:
    def tokens_list(word: str, separated: bool) -> List[Token]:
        if separated:
            return [
                Token(original=o, cleaned=clear_name(o)) for o in POSSIBLE_SEPARATORS.split(word)
            ]
        else:
            return [
                Token(original=word, cleaned=clear_name(" ".join(POSSIBLE_SEPARATORS.split(word))))
            ]

    return TokensSequence(
        list(
            itertools.chain.from_iterable(
                tokens_list(word, separated) for word in name.combined_name().original_tuple
            )
        )
    )


def _token_inclusion(name: TokensSequence, tokens: TokensSequence) -> Score:
    name_len = len(name.cleaned_name.split())
    return Score(
        value=float(
            len(name) == 1
            and name.cleaned_name in generate_words_subsets(tokens.cleaned_tuple, name_len)
            # subsets of words because i.e. ("abc de") must be found in ("abc", "de", "fgh"),
            # but ("ale yes") NOR in ("Gareth Bale Yesterday")
        ),
        compared=(name.original_tuple, tokens.original_tuple),
    )


def get_token_inclusion_score(first: NameInformation, second: NameInformation) -> Score:
    names = first.combined_name(), second.combined_name()
    if not all(names):
        return Score(compared=(names[0].original_tuple, names[1].original_tuple))

    words_length = sorted(len(name.original_tuple) for name in names)
    if words_length[0] != 1 or words_length[1] == 1:
        return Score(status=Score.ScoreStatus.NOT_APPLICABLE)

    return max(
        *(_token_inclusion(name, tokens) for name in _tokens(first) for tokens in _tokens(second)),
        *(
            reversed(_token_inclusion(name, tokens))
            for name in _tokens(second)
            for tokens in _tokens(first)
        )
    )
