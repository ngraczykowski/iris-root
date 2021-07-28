from typing import Dict, Optional, Sequence, Tuple, Union

from company_name.datasources.legal_terms import LEGAL_TERMS
from company_name.names.parse.create_tokens import create_tokens
from company_name.names.parse.cut_terms import cut_terms
from company_name.names.parse.extract_information import extract_countries, extract_weak
from company_name.names.tokens_sequence import TokensSequence

ParenthesisInformation = Union[str, TokensSequence]


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
        words, LEGAL_TERMS.legal_term_sources, with_weak_words=True
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
