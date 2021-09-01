import rapidfuzz.fuzz as fuzz

from company_name.knowledge_base import KnowledgeBase
from company_name.names.name_information import NameInformation, TokensSequence
from company_name.scores.score import Score


def _cast_to_common_base(tokens: TokensSequence) -> str:
    return " ".join(
        KnowledgeBase.common_suffixes.source_to_common_base.get(t, t) for t in tokens.cleaned_tuple
    )


def get_suffix_fuzzy_score(alerted_name: NameInformation, watchlist_name: NameInformation) -> Score:
    first, second = alerted_name.common_suffixes, watchlist_name.common_suffixes

    if not first and not second:
        return Score()

    return Score(
        value=fuzz.ratio(_cast_to_common_base(first), _cast_to_common_base(second)) / 100,
        compared=((first.original_name,), (second.original_name,)),
    )
