import dataclasses
import string
from typing import Tuple, Generator, Sequence

from company_name.names.special_words import WEAK_WORDS
from company_name.names.name_information import NameInformation, NameWord, NameSequence
from company_name.utils.clear_name import clear_name
from .score import Score


@dataclasses.dataclass
class Abbreviation:
    source: NameSequence
    abbreviated: NameSequence

    def compared(self) -> Tuple[Tuple[str, ...], Tuple[str, ...]]:
        return (self.source.original_name,), ("".join(self.abbreviated.original_tuple),)


def _check_abbreviation_for_next_word(
    word: NameWord,
    rest_of_information: Sequence[NameSequence],
    abbreviation: NameSequence,
    result: Abbreviation,
) -> Generator[Score, None, None]:
    # words such as of / the / ... are often omitted in abbreviation
    if not word or word in WEAK_WORDS:
        source = (result.source + [word]) if word else result.source
        yield check_abbreviation(
            rest_of_information,
            abbreviation,
            Abbreviation(source, abbreviated=result.abbreviated),
        )

    if word.cleaned[0] == abbreviation[0] or (word == "and" and abbreviation[0] == "&"):
        yield check_abbreviation(
            rest_of_information,
            abbreviation[1:],
            Abbreviation(
                source=result.source + [word],
                abbreviated=result.abbreviated + [abbreviation[0]],
            ),
        )

        # sometimes, for nicer abbreviation, more than one character from each word is taken
        for i, w in zip(range(5), word.cleaned):
            if len(abbreviation) > i and w == abbreviation[i].cleaned:
                yield check_abbreviation(
                    rest_of_information,
                    abbreviation[i + 1:],
                    Abbreviation(
                        source=result.source + [word],
                        abbreviated=result.abbreviated + abbreviation[:i + 1],
                    ),
                ) * (1 - 0.5 / len((*result.source, word, *rest_of_information)))
            else:
                break

    # 4H - Head, Heart, Hands, Health type of abbreviation
    if (
        abbreviation[0].cleaned in string.digits
        and len(abbreviation) > 1
        and abbreviation[1].cleaned not in string.digits
        and abbreviation[1].cleaned == word.cleaned[0]
    ):
        duplicate_by = int(abbreviation[0].cleaned) - 1
        abbreviated = NameWord(
            cleaned="", original="".join(abbreviation[:2].original_tuple)
        )
        new_abbreviation = (
            NameSequence(
                [NameWord(cleaned=abbreviation[1].cleaned, original="")] * duplicate_by
            )
            + abbreviation[2:]
        )
        yield check_abbreviation(
            rest_of_information,
            new_abbreviation,
            Abbreviation(
                source=result.source + [word],
                abbreviated=result.abbreviated + [abbreviated],
            ),
        )


def _check_abbreviation_when_no_abbreviation(
    words: NameSequence, result: Abbreviation
) -> Score:
    left_words = len(set(words).difference(WEAK_WORDS))
    if not left_words:
        return Score(value=1, compared=result.compared())

    if words[0] == "of":
        left_words -= 1
    return Score(
        value=max(1 - left_words / len(result.source), 0), compared=result.compared()
    )


def _check_abbreviation_when_no_words(
    rest_of_information: Sequence[NameSequence],
    abbreviation: NameSequence,
    result: Abbreviation,
) -> Generator[Score, None, None]:

    if rest_of_information:
        if rest_of_information[0]:
            # getting new words from common suffixes
            new_information = [
                rest_of_information[0][:1],
                rest_of_information[0][1:],
                *rest_of_information[1:],
            ]
            yield check_abbreviation(new_information, abbreviation, result)
        elif rest_of_information[1]:
            # getting abbreviation from legal
            legal_information = rest_of_information[1]
            if legal_information:
                # only first legal information is freely accessible in abbreviation
                # - next ones should be penalized
                yield check_abbreviation(
                    [NameSequence([legal_information[0]]), [], []], abbreviation, result
                )

    yield Score(
        value=max(1 - len(abbreviation) / len(result.abbreviated), 0),
        compared=result.compared(),
    )


def check_abbreviation(
    information: Sequence[NameSequence],
    abbreviation: NameSequence,
    result: Abbreviation,
) -> Score:
    words, *rest = information

    while abbreviation and not abbreviation[0].cleaned:
        result.abbreviated = result.abbreviated + abbreviation[:1]
        abbreviation = abbreviation[1:]
    
    while words and not words[0].cleaned:
        result.source = result.source + words[:1]
        words = words[1:]

    if not abbreviation:
        return _check_abbreviation_when_no_abbreviation(words, result)

    if not words:
        return max(
            _check_abbreviation_when_no_words(rest, abbreviation, result),
            default=Score(0, ((), ())),
        )

    return max(
        _check_abbreviation_for_next_word(
            words[0], (words[1:], *rest), abbreviation, result
        ),
        default=Score(0, ((), ())),
    )


def _abbreviation_score(
    information: NameInformation, abbreviation: NameSequence
) -> Score:
    words = NameSequence([*information.common_prefixes, *information.base])
    if (
        len("".join(abbreviation.cleaned_tuple)) >= len("".join(information.name().cleaned_name))
        or len(abbreviation) < 2
    ):
        return Score(None, ((), ()))

    return check_abbreviation(
        [
            words,
            information.common_suffixes,
            information.legal,
        ],
        abbreviation,
        Abbreviation(source=NameSequence(), abbreviated=NameSequence()),
    )


def _create_possible_abbreviation(name: str) -> NameSequence:
    return NameSequence(
        [NameWord(original=n, cleaned=clear_name(n)) for n in list(name)]
    )


def abbreviation_score(first: NameInformation, second: NameInformation) -> Score:
    return max(
        _abbreviation_score(
            first, _create_possible_abbreviation(second.base.original_name)
        ),
        reversed(
            _abbreviation_score(
                second, _create_possible_abbreviation(first.base.original_name)
            )
        ),
    )
