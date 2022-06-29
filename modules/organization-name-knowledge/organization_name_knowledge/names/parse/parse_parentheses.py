import re
from typing import Dict, Optional, Sequence, Tuple, Union

from organization_name_knowledge.knowledge_base import KnowledgeBase
from organization_name_knowledge.names.parse.create_tokens import create_tokens
from organization_name_knowledge.names.parse.cut_terms import cut_terms
from organization_name_knowledge.names.parse.extract_information import (
    extract_countries,
    extract_weak,
)
from organization_name_knowledge.names.parse.extract_legal_terms import extract_legal_terms
from organization_name_knowledge.names.tokens_sequence import TokensSequence
from organization_name_knowledge.utils.text import divide

ParenthesisInformation = Union[str, TokensSequence]
PARENTHESES_REGEX = r"[()\[\]]"


def _extract_known_parenthesis_information(
    name: str,
) -> Tuple[str, Optional[ParenthesisInformation]]:
    words = create_tokens(name)
    if not words:
        return "empty", None

    words_without_weak, _ = extract_weak(words)
    if not words_without_weak:
        return "empty", None

    words_without_legal, legal_terms = cut_terms(
        words, KnowledgeBase.legal_terms.legal_term_sources, with_weak_words=True
    )
    if not words_without_legal:
        return "legal", legal_terms

    words_without_countries, countries = extract_countries(words)
    if not words_without_countries:
        return "countries", countries

    return "unknown", name


def _combine_parentheses_information(
    information: Sequence[Tuple[str, Optional[ParenthesisInformation]]]
) -> Dict[str, ParenthesisInformation]:
    return {
        "legal": TokensSequence(
            [legal_term for k, v in information if k == "legal" for legal_term in v]
        ),
        "countries": TokensSequence(
            [legal_term for k, v in information if k == "countries" for legal_term in v]
        ),
        "unknown": [v for k, v in information if k == "unknown"],
    }


def detect_parentheses_information(
    names: Sequence[str],
) -> Dict[str, ParenthesisInformation]:
    return _combine_parentheses_information(
        [_extract_known_parenthesis_information(p) for p in names]
    )


def parse_base_from_parentheses(names: Sequence[str]) -> TokensSequence:
    all_words = []
    countries = create_tokens("")
    legal_terms = create_tokens("")
    for name in names:
        name_words = divide(name)
        all_words.extend(name_words)
        _find_countries_and_legal_in_subset(countries, legal_terms, name_words)
    valid_names = filter(
        lambda x: (x.lower() not in countries.cleaned_name)
        and (x.lower() not in legal_terms.cleaned_name)
        and (x.lower() not in KnowledgeBase.weak_words),
        all_words,
    )
    names_from_parenthesis = create_tokens(" ".join(valid_names))
    return names_from_parenthesis


def _find_countries_and_legal_in_subset(
    countries: TokensSequence,
    legal_terms: TokensSequence,
    name_words: Tuple[str],
):
    for sub_length in range(1, len(name_words) + 1):
        word_set = " ".join(name_words[0:sub_length])
        word_set = re.sub(PARENTHESES_REGEX, "", word_set).strip()

        country, legal = _check_for_legal_and_country(word_set)

        countries += country
        legal_terms += legal


def _check_for_legal_and_country(
    word_set: str,
) -> Tuple[TokensSequence, TokensSequence]:
    word_tokens = create_tokens(word_set)

    word_tokens, weak = extract_weak(word_tokens)
    if not word_tokens:
        return TokensSequence(), TokensSequence()

    word_tokens_start, legal, words_token_end = extract_legal_terms(word_tokens)
    word_tokens = word_tokens_start + words_token_end
    if not word_tokens:
        return TokensSequence(), legal

    word_tokens, country = extract_countries(word_tokens)
    return country, legal
