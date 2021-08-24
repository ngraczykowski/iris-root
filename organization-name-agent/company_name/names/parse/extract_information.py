from typing import Tuple

from company_name.knowledge_base import KnowledgeBase
from company_name.knowledge_base.term_sources import TermSources
from company_name.names.name_information import TokensSequence
from company_name.names.parse.cut_terms import cut_terms, cut_until_any_term_matches


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

    if end_legal and not start_legal.cleaned_name.endswith(end_legal.cleaned_name):
        return without_legal_at_the_end, end_legal, end_other

    if start_legal.cleaned_name.endswith(end_legal.cleaned_name):
        legal = start_legal
        without_legal_at_the_end = without_legal_at_the_end + end_other
        other = start_other
    else:
        legal = start_legal + end_legal
        other = start_other + end_other

    without_legal = [x for x in without_legal_at_the_end if x in without_legal_at_start]
    if len(legal.original_name.split()) >= 3:
        return TokensSequence(without_legal), legal, other
    else:
        return without_legal_at_the_end, end_legal, end_other

    # TODO refactor this all above


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


def extract_common(
    name: TokensSequence,
) -> Tuple[TokensSequence, TokensSequence, TokensSequence]:
    without_prefixes, common_prefixes = cut_terms(
        name, KnowledgeBase.common_prefixes.terms, from_start=True
    )
    if not without_prefixes:
        without_prefixes, common_prefixes = name, TokensSequence()
    if common_prefixes:
        pass

    without_suffixes, common_suffixes = cut_terms(
        without_prefixes,
        KnowledgeBase.common_suffixes.terms,
        saving_at_least_one_word=True,
    )
    if common_suffixes:
        pass

    return common_prefixes, without_suffixes, common_suffixes


def extract_countries(name: TokensSequence) -> Tuple[TokensSequence, TokensSequence]:
    return cut_terms(name, KnowledgeBase.countries.countries)


def extract_weak(name: TokensSequence):
    return cut_terms(name, TermSources(), with_weak_words=True)
