import itertools
from typing import Set

from organization_name_knowledge.utils.clear_name import remove_split_chars


def get_term_variants(term: str) -> Set[str]:
    def _single_word_variants(word: str):
        word_without_splits = remove_split_chars(word)
        return (
            word,
            word_without_splits,
            " ".join(word_without_splits),
        )

    return {
        term,
        *(
            " ".join(w).strip()
            for w in itertools.product(*[_single_word_variants(t) for t in term.split()])
        ),
    }
