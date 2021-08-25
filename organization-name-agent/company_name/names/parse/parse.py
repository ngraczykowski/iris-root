import re
from typing import Dict, Sequence, Tuple

from company_name.knowledge_base import KnowledgeBase
from company_name.names.name_information import NameInformation, Token, TokensSequence
from company_name.names.parse.create_tokens import create_tokens
from company_name.names.parse.extract_information import (
    extract_common,
    extract_weak,
)
from company_name.names.parse.extract_legal_terms import extract_legal_terms
from company_name.names.parse.parse_parentheses import detect_parentheses_information
from company_name.utils.clear_name import clear_name


def _fix_expression_divided(information: Tuple[TokensSequence, ...]) -> Tuple[TokensSequence, ...]:
    # handle common prefixes separately - words move right, not left as in other cases
    information = list(information)
    if information[0] and information[0][-1].cleaned in KnowledgeBase.joining_words:
        information[1] = TokensSequence([*information[0][-2:], *information[1]])
        information[0] = TokensSequence(information[0][:-2])

    data = information[1:]
    # move "and" to previous part, if at the beginning
    for joining_index, joining_data in enumerate(data):
        if (
            joining_index
            and joining_data
            and joining_data[0].cleaned in KnowledgeBase.joining_words
        ):
            possible_indexes = [i for i in reversed(range(joining_index)) if data[i]]
            if not possible_indexes:
                continue
            new_index = possible_indexes[0]
            data[new_index] = TokensSequence([*data[new_index], joining_data[0]])
            data[joining_index] = TokensSequence(joining_data[1:])

    # move word after "and" to part with "and"
    for joining_index, joining_data in enumerate(data):
        if joining_data and joining_data[-1].cleaned in KnowledgeBase.joining_words:

            second_word = None
            for j, second_word_data in enumerate(data[joining_index + 1 :]):
                if second_word_data:
                    second_word_index = j + joining_index + 1
                    second_word = second_word_data[0]
                    data[second_word_index] = TokensSequence(data[second_word_index][1:])
                    break

            if second_word:
                data[joining_index] = TokensSequence([*joining_data, second_word])

    return (information[0], *data)


def _detect_name_parts(name: str) -> Dict[str, TokensSequence]:
    name_tokens = create_tokens(name)

    name_without_weak, weak_words_at_the_end = extract_weak(name_tokens)
    if not name_without_weak:
        name_without_weak, weak_words_at_the_end = name_tokens, TokensSequence()
    name_without_legal, legal, other = extract_legal_terms(name_without_weak)
    common_prefixes, name_without_common, common_suffixes = extract_common(name_without_legal)

    information = _fix_expression_divided(
        (
            common_prefixes,
            name_without_common,
            common_suffixes,
            legal,
            other + weak_words_at_the_end,
        )
    )
    return {
        "common_prefixes": information[0],
        "base": information[1],
        "common_suffixes": information[2],
        "legal": information[3],
        "other": information[4],
    }


def _separate_parentheses(name: str) -> Tuple[str, Sequence[str]]:
    parentheses_regex = r"\(([^)]+)\)"
    parentheses = re.findall(parentheses_regex, name)
    name = re.sub(parentheses_regex, "", name).strip()
    return name, parentheses


def parse_name(name: str) -> NameInformation:
    original = name
    name_without_parentheses, parentheses = _separate_parentheses(name)

    base_information = _detect_name_parts(name_without_parentheses)
    parentheses_information = detect_parentheses_information(parentheses)
    parentheses_names = TokensSequence([parse_name(n) for n in parentheses_information["unknown"]])

    return NameInformation(
        source=Token(original=original, cleaned=clear_name(name)),
        common_prefixes=base_information["common_prefixes"],
        base=base_information["base"],
        common_suffixes=base_information["common_suffixes"],
        legal=base_information["legal"] + parentheses_information["legal"],
        countries=parentheses_information["countries"],
        parenthesis=parentheses_names,
        other=base_information["other"],
    )
