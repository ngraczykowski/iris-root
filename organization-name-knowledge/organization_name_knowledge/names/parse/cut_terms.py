from typing import Generator, Optional, Sequence, Tuple

from organization_name_knowledge.knowledge_base import KnowledgeBase
from organization_name_knowledge.knowledge_base.term_sources import TermSources
from organization_name_knowledge.names.token import Token
from organization_name_knowledge.names.tokens_sequence import TokensSequence


def _divide_name_for_possible_terms(
    name: TokensSequence, max_length: int, from_start: bool = False
) -> Generator[Tuple[TokensSequence, TokensSequence], None, None]:
    if from_start:
        last_probable_index = min(len(name), max_length)
        for i in reversed(range(0, last_probable_index + 1)):
            yield name[i:], name[:i]
    else:
        first_probable_index = max(0, len(name) - max_length)
        for i in range(first_probable_index, len(name)):
            yield name[:i], name[i:]


def _cut_one_term(
    name: TokensSequence, terms_to_cut: TermSources, from_start: bool = False
) -> Tuple[TokensSequence, Optional[Token]]:
    for name_left, possible_term in _divide_name_for_possible_terms(
        name, terms_to_cut.max_term_length, from_start
    ):
        if possible_term.cleaned_tuple in terms_to_cut:
            return name_left, Token.join(*possible_term)
    return name, None


def _cut_all_matching(
    name: TokensSequence, terms_to_cut: TermSources, from_start: bool = False
) -> Tuple[TokensSequence, Sequence[Token]]:
    found_terms = []
    while name:
        name, term = _cut_one_term(name, terms_to_cut, from_start=from_start)
        if term:
            found_terms.append(term)
        else:
            break

    return name, found_terms


def cut_terms(
    name: TokensSequence,
    terms_to_cut: TermSources,
    *,
    with_weak_words: bool = False,
    saving_at_least_one_word: bool = False,
    from_start: bool = False,
) -> Tuple[TokensSequence, TokensSequence]:
    if not name:
        return TokensSequence(), TokensSequence()

    if with_weak_words:
        terms_to_cut = terms_to_cut + TermSources({(w,) for w in KnowledgeBase.weak_words})

    name, saved_word = (
        next(_divide_name_for_possible_terms(name, 1, from_start=not from_start))
        if saving_at_least_one_word
        else (name, TokensSequence())
    )

    name_left, found_terms = _cut_all_matching(name, terms_to_cut, from_start=from_start)

    name_left = name_left + saved_word if from_start else saved_word + name_left
    if not from_start:
        found_terms = list(reversed(found_terms))

    return name_left, TokensSequence(found_terms)


def cut_until_any_term_matches(
    name: TokensSequence, terms_to_cut: TermSources, from_start: bool = False
) -> Tuple[TokensSequence, TokensSequence]:

    non_matching_tokens = []
    while name:
        _, term = _cut_one_term(name, terms_to_cut, from_start=from_start)
        if term:
            break

        if from_start:
            non_matching_tokens.append(name[0])
            name = name[1:]
        else:
            non_matching_tokens.append(name[-1])
            name = name[:-1]


    if not from_start:
        non_matching_tokens = list(reversed(non_matching_tokens))

    return name, TokensSequence(non_matching_tokens)
