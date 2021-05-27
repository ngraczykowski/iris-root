import re
from typing import Iterable, Tuple, Sequence

from company_name.utils.clear_name import clear_name, divide

from .legal_terms import LEGAL_TERMS
from .countries import COUNTRIES
from .common_prefixes import COMMON_PREFIXES
from .common_suffixes import COMMON_SUFFIXES
from .special_words import JOINING_WORDS, WEAK_WORDS

from .name_information import NameInformation, NameWord, NameSequence


def _find_occurrences(name: NameSequence, terms: Iterable[Sequence[str]]):
    words = set(name)
    possible_terms = {term for term in terms if all(word in words for word in term)}

    last_occurrence = -1
    for term in possible_terms:
        for i in reversed(range(len(name) - len(term) + 1)):
            if name[i : (i + len(term))] == term:
                last_occurrence = max(i + len(term) - 1, last_occurrence)
                break

    first_occurrence = last_occurrence
    if first_occurrence != -1:
        for term in possible_terms:
            for i in range(last_occurrence + 1):
                if name[i : (i + len(term))] == term:
                    first_occurrence = min(i, first_occurrence)
                    break

    return (
        first_occurrence if first_occurrence != -1 else None,
        last_occurrence if last_occurrence != -1 else None,
    )


def _cut_from_end(
    name: NameSequence, terms_to_cut: Iterable[Sequence[str]]
) -> Tuple[NameSequence, NameSequence]:
    possibilities = []
    for term in terms_to_cut:
        if name.endswith(term) and name != term:
            possibilities.append(term)
    if not possibilities:
        return name, NameSequence()

    best_one: Sequence[str] = sorted((len(x), x) for x in possibilities)[-1][1]
    if not best_one:
        return name, NameSequence()

    found_term = NameWord.join(*name[-len(best_one) :])
    base, other_terms = _cut_from_end(
        NameSequence(name[: -len(best_one)]), terms_to_cut
    )
    return base, other_terms + [found_term]


def _cut_from_start(
    name: NameSequence, terms_to_cut: Iterable[Sequence[str]]
) -> Tuple[NameSequence, NameSequence]:
    possibilities = []
    for term in terms_to_cut:
        if name.startswith(term) and name != term:
            possibilities.append(term)
    if not possibilities:
        return NameSequence(), name

    best_one = sorted((len(x[0]), x) for x in possibilities)[-1][1]
    found_term = NameWord.join(*name[: len(best_one)])
    other_terms, base = _cut_from_start(
        NameSequence(name[len(best_one) :]), terms_to_cut
    )
    return NameSequence((found_term, *other_terms)), base


def _fix_expression_divided(information: NameInformation) -> NameInformation:
    # handle common prefixes separately - words move right, not left as in other cases
    if (
        information.common_prefixes
        and information.common_prefixes[-1].cleaned in JOINING_WORDS
    ):
        information.base = NameSequence(
            [*information.common_prefixes[-2:], *information.base]
        )
        information.common_prefixes = NameSequence(information.common_prefixes[:-2])

    data = [information.base, information.common_suffixes, information.legal]
    # move "and" to previous part, if at the beginning
    for joining_index, joining_data in enumerate(data):
        if joining_index and joining_data and joining_data[0].cleaned in JOINING_WORDS:
            new_index = [i for i in reversed(range(joining_index)) if data[i]][0]
            data[new_index] = NameSequence([*data[new_index], joining_data[0]])
            data[joining_index] = NameSequence(joining_data[1:])

    # move word after "and" to part with "and"
    for joining_index, joining_data in enumerate(data):
        if joining_data and joining_data[-1].cleaned in JOINING_WORDS:

            second_word = None
            for j, second_word_data in enumerate(data[joining_index + 1 :]):
                if second_word_data:
                    second_word_index = j + joining_index + 1
                    second_word = second_word_data[0]
                    data[second_word_index] = NameSequence(data[second_word_index][1:])
                    break

            if second_word:
                data[joining_index] = NameSequence([*joining_data, second_word])

    information.base, information.common_suffixes, information.legal = data
    return information


def _cut_legal_terms(
    name: NameSequence,
) -> Tuple[NameSequence, NameSequence, NameSequence]:
    legal_terms = LEGAL_TERMS.legal_term_sources
    first_occurrence, last_occurrence = _find_occurrences(name, legal_terms)
    if last_occurrence is not None:
        if len(name) - last_occurrence - 1 <= first_occurrence:
            name, insignificant = (
                name[: last_occurrence + 1],
                name[last_occurrence + 1 :],
            )
        else:
            insignificant = []

        return (*_cut_from_end(name, {*legal_terms, *WEAK_WORDS}), insignificant)
    else:
        return name, NameSequence(), NameSequence()


def _cut_common(name: NameSequence) -> Tuple[NameSequence, NameSequence, NameSequence]:
    common_base, common_suffixes = _cut_from_end(name, COMMON_SUFFIXES)
    common_prefixes, base = _cut_from_start(common_base, COMMON_PREFIXES)
    return common_prefixes, base, common_suffixes


def _cut_extra(name: str) -> Tuple[str, Sequence[str]]:
    extra = list(re.finditer(r"\([^)]+\)", name))
    t = []
    for extra_information in reversed(extra):
        pos = extra_information.span()
        t.append(name[pos[0] + 1 : pos[1] - 1].strip())
        name = name[: pos[0]].strip() + " " + name[pos[1] :].strip()
    return name.strip(), t


def _cut_all(name: str) -> NameInformation:
    original = name
    extra_base, extra = _cut_extra(name)

    words = NameSequence(
        [
            NameWord(original=w, cleaned=clear_name(w))
            for w in divide(extra_base)
            if clear_name(w)
        ]
    )
    extra_words = NameSequence(
        [NameWord(original=w, cleaned=clear_name(w)) for w in extra]
    )

    if words.endswith(("the",)):
        words = NameSequence([words[-1], *words[:-1]])

    legal_base, legal, other = _cut_legal_terms(words)
    common_prefixes, common_base, common_suffixes = _cut_common(legal_base)
    information = NameInformation(
        source=NameWord(original=original, cleaned=clear_name(original)),
        common_prefixes=common_prefixes,
        base=common_base,
        common_suffixes=common_suffixes,
        legal=legal,
        countries=NameSequence(
            [c for c in extra_words if c.cleaned in COUNTRIES.mapping]
        ),
        parenthesis=NameSequence(
            [c for c in extra_words if c.cleaned not in COUNTRIES.mapping]
        ),
        other=other,
    )
    return _fix_expression_divided(information)


def parse_name(name: str) -> NameInformation:
    return _cut_all(name)
