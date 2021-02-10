import collections
import re
import string

from typing import *  # sorry

import fuzzywuzzy.fuzz
import unidecode


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
    "international",
}

WEAK_WORDS = {"of", "the", "for", "and"}

NameInformation = collections.namedtuple(
    "NameInformation", ("base", "common_suffixes", "legal", "parenthesis_information")
)


def clear_name(name):
    name = (
        unidecode.unidecode(name.lower())
        .strip()
        .strip(",.")
        .replace(", ", " ")
        .replace(". ", " ")
    )
    return re.sub(r"\.\w{2,3}\b", "", name)  # .net, .com, .org, .de


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


def _check_abbreviation_for_next_word(
    word, rest_of_information, abbreviation, length_of_all
):
    # words such as of / the / ... are often omitted in abbreviation
    if not word or word in WEAK_WORDS:
        yield check_abbreviation(rest_of_information, abbreviation, length_of_all)

    if word[0] == abbreviation[0] or (word == "and" and abbreviation[0] == "&"):
        yield check_abbreviation(rest_of_information, abbreviation[1:], length_of_all)

        # sometimes, for nicer abbreviation, more than one character from each word is taken
        for i, w in zip(range(5), word):
            if len(abbreviation) > i and w == abbreviation[i]:
                yield check_abbreviation(
                    rest_of_information, abbreviation[i + 1 :], length_of_all
                ) * (1 - 0.5 / length_of_all)
            else:
                break

    # 4H - Head, Heart, Hands, Health type of abbreviation
    if (
        abbreviation[0] in string.digits
        and len(abbreviation) > 1
        and abbreviation[1] not in string.digits
        and abbreviation[1] == word[0]
    ):
        duplicate_by = int(abbreviation[0]) - 1
        yield check_abbreviation(
            rest_of_information,
            duplicate_by * abbreviation[1] + abbreviation[2:],
            length_of_all,
        )


def _check_abbreviation_when_no_abbreviation(words, length_of_all):
    left_words = len(set(words).difference(WEAK_WORDS))
    if not left_words:
        return 1

    if words[0] == "of":
        left_words -= 1
    return 1 - left_words / length_of_all


def _check_abbreviation_when_no_words(rest_of_information, abbreviation, length_of_all):
    if rest_of_information:
        if rest_of_information[0]:
            # getting new words from common suffixes
            new_information = [
                rest_of_information[0][0].split(),
                rest_of_information[0][1:],
                *rest_of_information[1:],
            ]
            yield check_abbreviation(new_information, abbreviation, length_of_all)
        elif rest_of_information[1]:
            # getting abbreviation from legal
            legal_information = rest_of_information[1]
            if legal_information:
                # only first legal information is freely accessible in abbreviation - next ones should be penalized
                yield check_abbreviation([[legal_information[0]], [], []], abbreviation, length_of_all)

    yield 1 - len(abbreviation) / length_of_all


def check_abbreviation(information, abbreviation, length_of_all: int) -> float:
    words, *rest = information

    if not abbreviation:
        return _check_abbreviation_when_no_abbreviation(words, length_of_all)

    if not words:
        return max(
            _check_abbreviation_when_no_words(rest, abbreviation, length_of_all),
            default=0,
        )

    return max(
        _check_abbreviation_for_next_word(
            words[0], [words[1:], *rest], abbreviation, length_of_all
        ),
        default=0,
    )


def legal_score(first, second) -> float:
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
    words = information.base.split()
    if len(abbreviation) > 0.5 * len(information.base) or len(abbreviation) < 2 or not words:
        return 0

    return check_abbreviation(
        [
            words,
            information.common_suffixes,
            information.legal,
        ],
        abbreviation.replace(".", "").replace(" ", ""),
        length_of_all=len(words),
    )


def _names_to_compare(
    first: NameInformation, second: NameInformation
) -> Tuple[str, str]:
    def _join(base, suffixes):
        return " ".join((base, *suffixes))

    if not first.common_suffixes or not second.common_suffixes:
        yield first.base, second.base

    shortest_length = min(len(first.common_suffixes), len(second.common_suffixes))
    if (
        first.common_suffixes[:shortest_length]
        == second.common_suffixes[:shortest_length]
    ):
        yield _join(first.base, first.common_suffixes[:shortest_length]), _join(
            second.base, second.common_suffixes[:shortest_length]
        )

    yield _join(first.base, first.common_suffixes), _join(
        second.base, second.common_suffixes
    )


def tokenization_score(first: NameInformation, second: NameInformation) -> float:
    result = 0
    for first_name, second_name in _names_to_compare(first, second):
        if not first_name or not second_name:
            continue
        first_tokens, second_tokens = set(first_name.split()), set(second_name.split())
        common = first_tokens.intersection(second_tokens)
        different = first_tokens.symmetric_difference(second_tokens).difference(
            WEAK_WORDS
        )
        result = max(
            result,
            len(common) / (len(common) + len(different)),
        )
    return result


def fuzzy_score(
    first: NameInformation, second: NameInformation, fuzz_function="ratio"
) -> float:
    result = 0
    for first_name, second_name in _names_to_compare(first, second):
        result = max(
            result,
            getattr(fuzzywuzzy.fuzz, fuzz_function)(first_name, second_name),
        )
    return result / 100


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
        "fuzzy": fuzzy_score(first_information, second_information, "ratio"),
        "partial_fuzzy": fuzzy_score(
            first_information, second_information, "partial_ratio"
        ),
        "sorted_fuzzy": fuzzy_score(
            first_information, second_information, "token_sort_ratio"
        ),
        "legal_terms": legal_score(first_information.legal, second_information.legal),
        "tokenization": tokenization_score(first_information, second_information),
    }


def compare(
    first,
    second,
    fuzzy_match_threshold=0.8,
    abbreviate_threshold=0.7,
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
