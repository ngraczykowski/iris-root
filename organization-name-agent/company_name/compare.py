import collections
import re
import string

from typing import *  # sorry

import fuzzywuzzy.fuzz


LEGAL_TERMS = {
    "co": "company",
    "lp": "limited partnership",
    "llp": "limited liability partnership",
    "lllp": "limited liability limited partnership",
    "ltd": "limited liability company",
    "lc": "limited liability company",
    "llc": "limited liability company",
    "tld co": "limited liability company",
    "pplc": "professional limited liability company",
    "plc": "public limited company",
    "limited": "limited liability company",
    "corp": "corporation",
    "pvt": "private",
    "inc": "incorporated",
    "incorporation": "incorporated",
    "gmbh": "gesellschaft mit beschränkter haftung",
    "ag": "aktiengesellschaft",
}
TERMS = {*LEGAL_TERMS.keys(), *LEGAL_TERMS.values()}

COMMON = {
    "pharmaceuticals",
    "technologies",
    "association",
    "group",
    "devices",
    "communications",
    "information",
    "technology",
    "motor",
    "computer",
}

NameInformation = collections.namedtuple(
    "NameInformation", ("base", "common_suffixes", "legal", "parenthesis_information")
)


def clear_name(name):
    return name.lower().strip().strip(",.").replace(", ", " ").replace(". ", " ")


def cut_something(name, bag_of_something):
    possibilities = []
    for term in bag_of_something:
        if name.endswith(" " + term):
            possibilities.append(term)
    if not possibilities:
        return name, []
    best_one = sorted([(len(x), x) for x in possibilities])[-1][1]
    base, legal_term = cut_something(name[: -len(best_one)].strip(), bag_of_something)
    return base.strip(), (*legal_term, best_one)


def cut_legal_terms(name):
    return cut_something(name, TERMS)


def cut_common(name):
    return cut_something(name, COMMON)


def cut_extra(name):
    extra = list(re.finditer(r"\([^)]+\)", name))
    if not extra:
        return name, []
    t = []
    for extra_information in reversed(extra):
        pos = extra_information.span()
        t.append(name[pos[0] + 1 : pos[1] - 1])
        name = name[: pos[0]].strip() + " " + name[pos[1] :].strip()
    return name.strip(), t


def cut_all(name) -> NameInformation:
    extra_base, extra = cut_extra(name)
    legal_base, legal = cut_legal_terms(extra_base)
    common_base, common = cut_common(legal_base)
    return NameInformation(
        base=common_base,
        common_suffixes=common,
        legal=legal,
        parenthesis_information=extra,
    )


def check_abbreviation(information, abbreviation) -> float:
    words, *rest = information
    max_result = 0.0

    if not words:
        if not abbreviation:
            return 1
        if rest:
            if rest[0]:
                new_information = [[rest[0][0]], rest[0][1:], *rest[1:]]
            else:
                new_information = [[], *rest[1:]]
            max_result = max(
                max_result, 0.9 * check_abbreviation(new_information, abbreviation)
            )

        return max(max_result, 1 / (1 + len(abbreviation)))

    if not abbreviation:
        if words[0] == "of":
            return 0.8
        return 1 / (1 + len(words))

    if abbreviation[0] in string.digits:
        duplicate_by = int(abbreviation[0])
        max_result = max(
            max_result,
            check_abbreviation(
                information, duplicate_by * abbreviation[1] + abbreviation[2:]
            ),
        )

    if abbreviation[0] == " ":
        return check_abbreviation(information, abbreviation[1:])

    word = words[0]
    if word[0] == abbreviation[0] or (word == "and" and abbreviation[0] == "&"):
        max_result = max(
            max_result, check_abbreviation([words[1:], *rest], abbreviation[1:])
        )
        for i, w in enumerate(word):
            if len(abbreviation) > i and w == abbreviation[i]:
                max_result = max(
                    max_result,
                    0.9 * check_abbreviation([words[1:], *rest], abbreviation[i + 1 :]),
                )
            else:
                break
    if word in {"of", "the", "for", "and"}:
        max_result = max(
            max_result, check_abbreviation([words[1:], *rest], abbreviation)
        )

    print(information, abbreviation, max_result)
    return max_result


def legal_score(first, second) -> float:
    # if not first and not second:
    #    return 1
    if not first or not second:
        return 0.5
    norm_first = [LEGAL_TERMS.get(t, t) for t in first]
    norm_second = [LEGAL_TERMS.get(t, t) for t in second]
    if norm_first == norm_second:
        return 1
    if set(norm_first).issuperset(norm_second):
        return 0.5 * (1 + len(norm_second) / len(norm_first))
    if set(norm_second).issuperset(norm_first):
        return 0.5 * (1 + len(norm_first) / len(norm_second))
    return 0


def abbreviation_score(information, abbreviation) -> float:
    return (
        (
            check_abbreviation(
                [
                    information.base.split(" "),
                    information.common_suffixes,
                    information.legal,
                ],
                abbreviation.replace(".", ""),
            )
            * max(1, (len(abbreviation) + 7) / 10)
        )
        if len(abbreviation) < 0.5 * len(information.base)
        else 0
    )


def _names_to_compare(
    first: NameInformation, second: NameInformation
) -> Tuple[str, str]:
    def _join(base, suffixes):
        return " ".join((base, *suffixes))

    if not first.common_suffixes or not second.common_suffixes:
        return first.base, second.base
    shortest_length = min(len(first.common_suffixes), len(second.common_suffixes))
    if (
        first.common_suffixes[:shortest_length]
        == second.common_suffixes[:shortest_length]
    ):
        return _join(first.base, first.common_suffixes[:shortest_length]), _join(
            second.base, second.common_suffixes[:shortest_length]
        )

    return _join(first.base, first.common_suffixes), _join(
        second.base, second.common_suffixes
    )


def tokenization_score(first: NameInformation, second: NameInformation) -> float:
    first_name, second_name = _names_to_compare(first, second)
    first_tokens, second_tokens = set(first_name.split()), set(second_name.split())
    return len(first_tokens.intersection(second_tokens)) / len(
        first_tokens.union(second_tokens)
    )


def fuzzy_score(first: NameInformation, second: NameInformation) -> float:
    return fuzzywuzzy.fuzz.ratio(*_names_to_compare(first, second)) / 100


def score(first, second):
    first_information = cut_all(clear_name(first))
    first_base = first_information.base
    second_information = cut_all(clear_name(second))
    second_base = second_information.base

    extra_match = float(
        second_base in first_information.parenthesis_information
        or first_base in second_information.parenthesis_information
    )
    abbreviate_score = max(
        abbreviation_score(first_information, second_base),
        abbreviation_score(second_information, first_base),
    )

    return {
        "parenthesis_match": extra_match,
        "abbreviation": abbreviate_score,
        "fuzzy": fuzzy_score(first_information, second_information),
        "legal_terms": legal_score(first_information.legal, second_information.legal),
        "tokenization": tokenization_score(first_information, second_information),
    }


def compare(
    first,
    second,
    fuzzy_match_threshold=0.8,
    abbreviate_threshold=0.8,
    tokenization_threshold=0.8,
):
    scored = score(first, second)

    return (
        (scored["fuzzy"] >= fuzzy_match_threshold)
        or (scored["abbreviation"] >= abbreviate_threshold)
        or (
            scored["parenthesis_match"]
            and scored["abbreviation"] >= 0.5 * abbreviate_threshold
        )
        or (scored["tokenization"] >= tokenization_threshold)
    )
