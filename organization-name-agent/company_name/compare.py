import dataclasses
import itertools
import re
import string
from typing import Mapping, Set, Sequence, Tuple, Iterable

import fuzzywuzzy.fuzz as fuzz
import phonetics
import unidecode

try:
    from .countries import COUNTRY_MAPPING
    from .legal_terms import ALL_LEGAL_TERMS, legal_score
except ImportError:
    from countries import COUNTRY_MAPPING
    from legal_terms import ALL_LEGAL_TERMS, legal_score


BLACKLIST = {"gazprom", "vtb"}
BLACKLIST_REGEX = re.compile(r"\b(" + "|".join(BLACKLIST) + r")\b", re.IGNORECASE)

JOINING_WORDS = ("and", "&", "e", "y", "und", "ve")
WEAK_WORDS = {"of", "the", "for", "de", *JOINING_WORDS}

COMMON_PREFIXES = {
    "company",
    "group",
    "grup",
    "grupo",
    *WEAK_WORDS,
}

COMMON_SUFFIXES = {
    "asset",
    "association",
    "authorised distributor",
    "banking",
    "brothers",
    "capital",
    "comercio",
    "commercial",
    "communication",
    "communications",
    "computer",
    "construction",
    "consultancy",
    "consultants",
    "consulting",
    "contracting",
    "credit",
    "development",
    "devices",
    "energia",
    "energy",
    "engineering",
    "enterprise",
    "enterprises",
    "equity",
    "export",
    "exports",
    "factory",
    "finance",
    "financial",
    "fund",
    "general",
    "global",
    "group",
    "grup",
    "grupo",
    "holding",
    "holdings",
    "import",
    "industria",
    "industrial",
    "industries",
    "industry",
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
    "products",
    "regional",
    "sanayi",
    "securities",
    "service",
    "services",
    "shipping",
    "solutions",
    "stock",
    "studio",
    "support",
    "systems",
    "tech",
    "technologies",
    "technology",
    "ticaret",
    "trade",
    "trading",
    "trust",
    "universal",
    *WEAK_WORDS
}


@dataclasses.dataclass
class NameInformation:
    common_prefixes: Tuple[str, ...]
    base: Tuple[str, ...]
    common_suffixes: Tuple[str, ...]
    legal: Tuple[str, ...]
    countries: Tuple[str, ...]
    parenthesis: Tuple[str, ...]


def clear_name(name: str) -> str:
    name = unidecode.unidecode(name.lower()).replace(",", " ").replace(" -", " ").replace("- ", " ").replace("  ", " ").strip()
    return re.sub(r"\.\w{2,3}\b", "", name)  # .net, .com, .org, .de


def cut_from_end(name: str, terms_to_cut: Iterable[str]):
    possibilities = []
    for term in terms_to_cut:
        if name.endswith(" " + term):
            possibilities.append(term)
    if not possibilities:
        return name, ()
    best_one = sorted([(len(x), x) for x in possibilities])[-1][1]
    base, other_terms = cut_from_end(name[: -len(best_one)].strip(), terms_to_cut)
    return base.strip(), (*other_terms, best_one)


def cut_from_start(name: str, terms_to_cut: Iterable[str]):
    possibilities = []
    for term in terms_to_cut:
        if name.startswith(term + " "):
            possibilities.append(term)
    if not possibilities:
        return (), name
    best_one = sorted([(len(x[0]), x) for x in possibilities])[-1][1]
    other_terms, base = cut_from_start(name[len(best_one):].strip(), terms_to_cut)
    return (best_one, *other_terms), base.strip()


def fix_expression_divided(information: NameInformation) -> NameInformation:
    # handle common prefixes separately - words move right, not left as in other cases
    if information.common_prefixes and information.common_prefixes[-1] in JOINING_WORDS:
        information.base = tuple((*information.common_prefixes[-2:], *information.base))
        information.common_prefixes = information.common_prefixes[:-2]

    data = [information.base, information.common_suffixes, information.legal]
    # move "and" to previous part, if at the beginning
    for joining_index, joining_data in enumerate(data):
        if joining_index and joining_data and joining_data[0] in JOINING_WORDS:
            new_index = [i for i in reversed(range(joining_index)) if data[i]][0]
            data[new_index] = tuple((*data[new_index], joining_data[0]))
            data[joining_index] = joining_data[1:]

    # move word after "and" to part with "and"
    for joining_index, joining_data in enumerate(data):
        if joining_data and joining_data[-1] in JOINING_WORDS:
            for j, second_word_data in enumerate(data[joining_index+1:]):
                if second_word_data:
                    second_word_index = j + joining_index + 1
                    second_word = second_word_data[0]
                    data[second_word_index] = data[second_word_index][1:]
                    break
            else:
                second_word = None

            if second_word:
                data[joining_index] = tuple((*joining_data, second_word))

    information.base, information.common_suffixes, information.legal = data
    return information


def cut_legal_terms(name):
    return cut_from_end(name, ALL_LEGAL_TERMS)


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
    if name.endswith(" the"):
        name = "the " + name[:-4]
    extra_base, extra = cut_extra(name)
    legal_base, legal = cut_legal_terms(extra_base)
    common_prefixes, common_base, common_suffixes = cut_common(legal_base)
    base = tuple(common_base.split())
    information = NameInformation(
        common_prefixes=common_prefixes,
        base=base,
        common_suffixes=common_suffixes,
        legal=legal,
        countries=tuple(c for c in extra if c in COUNTRY_MAPPING),
        parenthesis=tuple(c for c in extra if c not in COUNTRY_MAPPING),
    )
    return fix_expression_divided(information)


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

    return scores, (dataclasses.asdict(first_information), dataclasses.asdict(second_information))
