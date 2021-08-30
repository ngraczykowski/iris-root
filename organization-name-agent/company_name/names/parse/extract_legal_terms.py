from typing import Tuple

from company_name.knowledge_base import KnowledgeBase
from company_name.knowledge_base.term_sources import TermSources
from company_name.names.parse.cut_terms import cut_terms, cut_until_any_term_matches
from company_name.names.tokens_sequence import TokensSequence


def extract_legal_terms(
    name: TokensSequence,
) -> Tuple[TokensSequence, TokensSequence, TokensSequence]:

    legal_terms = KnowledgeBase.legal_terms.legal_term_sources

    without_legal_at_the_end, end_legal, end_other = _extract_legal_from_chosen_name_part(
        name, legal_terms, from_start=False
    )

    without_legal_at_start, start_legal, start_other = _extract_legal_from_chosen_name_part(
        name, legal_terms, from_start=True
    )

    if start_legal.endswith(end_legal.cleaned_name) and start_legal != end_legal:
        if _check_is_legal_part_of_name(start_legal):
            return start_legal + without_legal_at_start, start_legal, start_other
        else:
            return without_legal_at_start, start_legal, start_other
    else:
        return without_legal_at_the_end, end_legal, end_other


def _extract_legal_from_chosen_name_part(
    name: TokensSequence, legal_terms: TermSources, from_start: bool
) -> Tuple[TokensSequence, TokensSequence, TokensSequence]:

    base, other = cut_until_any_term_matches(name, legal_terms, from_start)
    if not base or len(base) <= len(other):
        return name, TokensSequence(), TokensSequence()

    without_legal, legal = cut_terms(
        base,
        legal_terms,
        with_weak_words=True,
        saving_at_least_one_word=True,
        from_start=from_start,
    )
    return without_legal, legal, other


def _check_is_legal_part_of_name(legal: TokensSequence) -> bool:
    return len(legal.cleaned_name.split()) < 3 or any(x in legal for x in ["of", "and"])
