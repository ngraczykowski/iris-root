from typing import Tuple

from organization_name_knowledge.knowledge_base import KnowledgeBase
from organization_name_knowledge.knowledge_base.term_sources import TermSources
from organization_name_knowledge.names.parse.cut_terms import cut_terms
from organization_name_knowledge.names.tokens_sequence import TokensSequence


def extract_common(
    name: TokensSequence,
) -> Tuple[TokensSequence, TokensSequence, TokensSequence]:

    without_prefixes, common_prefixes = cut_terms(
        name, KnowledgeBase.common_prefixes.terms, from_start=True
    )
    if not without_prefixes:
        without_prefixes, common_prefixes = name, TokensSequence()

    without_suffixes, common_suffixes = cut_terms(
        without_prefixes,
        KnowledgeBase.common_suffixes.terms,
        saving_at_least_one_word=True,
    )

    return common_prefixes, without_suffixes, common_suffixes


def extract_countries(name: TokensSequence) -> Tuple[TokensSequence, TokensSequence]:
    return cut_terms(name, KnowledgeBase.countries.countries)


def extract_weak(name: TokensSequence) -> Tuple[TokensSequence, TokensSequence]:
    return cut_terms(name, TermSources(), with_weak_words=True)
