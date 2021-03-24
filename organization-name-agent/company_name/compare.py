import collections
import itertools
import re
import string
from typing import Mapping, Set, Sequence, Tuple

import fuzzywuzzy.fuzz as fuzz
import phonetics
import unidecode

try:
    from .countries import COUNTRY_MAPPING
except ImportError:
    from countries import COUNTRY_MAPPING


BLACKLIST = {"gazprom", "vtb"}
BLACKLIST_REGEX = re.compile(r"\b(" + "|".join(BLACKLIST) + r")\b", re.IGNORECASE)

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
    "pc": "professional corporation",
    "pvt": "private",
    "inc": "incorporated",
    "incorporation": "incorporated",
    "pty": "unlimited proprietary",
    "ilp": "incorporated limited partnership",
    "gmbh": "gesellschaft mit beschrankter haftung",
    "ag": "aktiengesellschaft",
    "ab": "aktiebolag",
    "as": "aksjeselskap",
    "ans": "ansvarlig selskap",
    "nv": "naamloze vennootschap",
    "kk": "kabushiki gaisha",
    "ek": "ekonomisk forening",
    "hb": "handelsbolag",
    "kb": "kommanditbolag",
    "bv": "besloten vennootschap",
    "cv": "commanditaire vennootschap",
    "sa": "societe anonyme",
    "sociedad anonima": "societe anonyme",
    "de cv": "capital variable",
    "ltda": "limited liability company",
    "ooo": "obschestvo s ogranichennoy otvetstvennostyu",
    "pao": "publichnoye aktsionernoye obschestvo",
    "oao": "otkrytoye aktsionernoye obschestvo",
    "sas": "sociedad por acciones simplificada",
    "srl": "sociedad de responsabilidad limitada",
    "sci": "sociedad de capital e industria",
    "sc": "sociedad colectiva",
    "sarl": "societe a responsabilite limitee",
    "cic": "community interest company",
    "cio": "charitable incorporated organisation",
    "jsc": "joint-stock company",
    "ojsc": "open joint-stock company",
    "pjsc": "public joint-stock company",
    "fze": "free zone establishment",
    "fzco": "free zone company",
    "soe": "state-owned enterprise",
    "goe": "government-owned enterprise",
    "sdn bhd": "sendirian berhad",
    "bhd": "berhad",
}

TERMS = {*LEGAL_TERMS.keys(), *LEGAL_TERMS.values()}

WEAK_WORDS = {"of", "the", "for", "and"}

COMMON_SUFFIXES = {
    "asset",
    "association",
    "bank",
    "banking",
    "brothers",
    "capital",
    "commercial",
    "communication",
    "communications",
    "construction",
    "consulting",
    "computer",
    "credit",
    "development",
    "devices",
    "energy",
    "engineering",
    "enterprise",
    "enterprises",
    "equity",
    "export",
    "exports",
    "finance",
    "financial",
    "fund",
    "general",
    "global",
    "group",
    "holding",
    "holdings",
    "industries",
    "information",
    "international",
    "invest",
    "investment",
    "investments",
    "joint",
    "logistics",
    "management",
    "manufacturing",
    "marketing",
    "media",
    "partners",
    "pharmaceuticals",
    "regional",
    "securities",
    "services",
    "shipping",
    "stock",
    "support",
    "tech",
    "technologies",
    "technology",
    "trade",
    "trading",
    "trust",
    *WEAK_WORDS
}

COMMON_PREFIXES = COMMON_SUFFIXES

NameInformation = collections.namedtuple(
    "NameInformation",
    ("common_prefixes", "base", "common_suffixes", "legal", "countries", "parenthesis")
)


def clear_name(name: str) -> str:
    name = unidecode.unidecode(name.lower()).replace(",", " ").strip()
    return re.sub(r"\.\w{2,3}\b", "", name)  # .net, .com, .org, .de


def term_variants(term: str) -> Set[str]:
    return {
        term,
        term + ".",
        *(
            " ".join(w).strip()
            for w in itertools.product(
                *[
                    (" ".join(t), "".join(t), ". ".join(t) + ".", ".".join(t) + ".")
                    for t in term.split()
                ]
            )
        ),
    }


def cut_from_end(name: str, terms_to_cut):
    possibilities = []
    for term in terms_to_cut:
        for term_variant in term_variants(term):
            if name.endswith(" " + term_variant):
                possibilities.append((term_variant, term))
    if not possibilities:
        return name, ()
    best_one = sorted([(len(x[0]), x) for x in possibilities])[-1][1]
    base, other_terms = cut_from_end(name[: -len(best_one[0])].strip(), terms_to_cut)
    return base.strip(), (*other_terms, best_one[1])


def cut_from_start(name: str, terms_to_cut):
    possibilities = []
    for term in terms_to_cut:
        for term_variant in term_variants(term):
            if name.startswith(term_variant + " "):
                possibilities.append((term_variant, term))
    if not possibilities:
        return (), name
    best_one = sorted([(len(x[0]), x) for x in possibilities])[-1][1]
    other_terms, base = cut_from_start(name[len(best_one[0]):].strip(), terms_to_cut)
    return (best_one[1], *other_terms), base.strip()


def cut_legal_terms(name):
    return cut_from_end(name, TERMS)


def cut_common(name):
    common_base, common_suffixes = cut_from_end(name, COMMON_SUFFIXES)
    common_prefixes, base = cut_from_start(common_base, COMMON_PREFIXES)
    return common_prefixes, base, common_suffixes


def cut_extra(name):
    extra = list(re.finditer(r"\([^)]+\)", name))
    t = []
    for extra_information in reversed(extra):
        pos = extra_information.span()
        t.append(name[pos[0] + 1:pos[1] - 1].strip())
        name = name[:pos[0]].strip() + " " + name[pos[1]:].strip()
    return name.strip(), t


def cut_all(name) -> NameInformation:
    extra_base, extra = cut_extra(name)
    legal_base, legal = cut_legal_terms(extra_base)
    common_prefixes, common_base, common_suffixes = cut_common(legal_base)
    return NameInformation(
        common_prefixes=common_prefixes,
        base=tuple(common_base.split()),
        common_suffixes=common_suffixes,
        legal=legal,
        countries=tuple(c for c in extra if c in COUNTRY_MAPPING),
        parenthesis=tuple(c for c in extra if c not in COUNTRY_MAPPING),
    )


def _without_whitespaces(*names):
    return ("".join(name.split()) for name in names)


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
                    rest_of_information, abbreviation[i + 1:], length_of_all
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
                # only first legal information is freely accessible in abbreviation
                # - next ones should be penalized
                yield check_abbreviation(
                    [[legal_information[0]], [], []], abbreviation, length_of_all
                )

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


def abbreviation_score(information: NameInformation, abbreviation: str) -> float:
    words = [*information.common_prefixes, *information.base]
    if (
        len(abbreviation) >= len("".join(information.base))
        or len(abbreviation) < 2
        or not words
    ):
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


def tokenization_score(first_name: str, second_name: str, absolute=False) -> float:
    if not first_name or not second_name:
        return 0

    first_tokens, second_tokens = set(first_name.split()), set(second_name.split())
    common = first_tokens.intersection(second_tokens)
    different = first_tokens.symmetric_difference(second_tokens).difference(WEAK_WORDS)
    return len(common) if absolute else len(common) / (len(common) + len(different))


def phonetic_score(first_name: str, second_name: str) -> float:
    scores = [
        fuzz.ratio(n1, n2)
        for n1, n2 in itertools.product(
             phonetics.dmetaphone(first_name), phonetics.dmetaphone(second_name)
        )
        if n1 and n2
    ]
    return max(scores) / 100 if scores else 0


def _get_countries(parenthesis_information: Sequence[str]) -> Set[int]:
    return set(itertools.chain(*(
        COUNTRY_MAPPING.get(c.strip(), []) for c in parenthesis_information
    )))


def country_score(first_countries, second_countries) -> float:
    first_country_ids, second_country_ids = (
        _get_countries(first_countries),
        _get_countries(second_countries),
    )

    if not first_country_ids or not second_country_ids:
        return 0.5

    return float(bool(first_country_ids.intersection(second_country_ids)))


def compare(
        first: str, second: str
) -> Tuple[Mapping[str, float], Tuple[Mapping[str, Tuple[str, ...]], Mapping[str, Tuple[str, ...]]]]:

    blacklisted = float(bool(BLACKLIST_REGEX.search(first) or BLACKLIST_REGEX.search(second)))

    first_information = cut_all(clear_name(first))
    first_base = " ".join(first_information.base)
    first = " ".join((
        *first_information.common_prefixes,
        *first_information.base,
        *first_information.common_suffixes
    ))

    second_information = cut_all(clear_name(second))
    second_base = " ".join(second_information.base)
    second = " ".join((
        *second_information.common_prefixes,
        *second_information.base,
        *second_information.common_suffixes
    ))

    extra_match = float(
        second_base in first_information.parenthesis
        or first_base in second_information.parenthesis
    )
    abbreviate_score = max(
        abbreviation_score(first_information, second_base),
        abbreviation_score(second_information, first_base),
    )

    scores = {
        "parenthesis_match": extra_match,
        "abbreviation": abbreviate_score,
        "fuzzy_on_base": fuzz.ratio(*_without_whitespaces(first_base, second_base)) / 100,
        "fuzzy": fuzz.ratio(*_without_whitespaces(first, second)) / 100,
        "partial_fuzzy": fuzz.partial_ratio(*_without_whitespaces(first, second)) / 100,
        "sorted_fuzzy": fuzz.token_sort_ratio(*_without_whitespaces(first, second)) / 100,
        "legal_terms": legal_score(first_information.legal, second_information.legal),
        "tokenization": tokenization_score(first, second),
        "absolute_tokenization": tokenization_score(first, second, absolute=True),
        "blacklisted": blacklisted,
        "country": country_score(
            first_information.countries,
            second_information.countries,
        ),
        "phonetics_on_base": phonetic_score(first_base, second_base),
        "phonetics": phonetic_score(first, second),
    }

    return scores, (first_information._asdict(), second_information._asdict())
