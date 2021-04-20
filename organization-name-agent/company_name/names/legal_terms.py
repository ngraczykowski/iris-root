import collections
import itertools
import json
import pathlib
from typing import Mapping, Sequence, Set, Tuple, Iterable, List, Any

from company_name.utils.clear_name import clear_name, divide


LEGAL_TERMS_PATH = pathlib.Path(__file__).parent / "legal_terms.json"

LegalTerm = Tuple[str, ...]


flatten = itertools.chain.from_iterable


def _prepare_dict_from_list_with_duplicates(
    items: Iterable[Tuple[Any, Any]]
) -> Mapping[Any, List[Any]]:
    d = collections.defaultdict(list)
    for k, v in items:
        d[k].append(v)
    return dict(d)


class LegalTerms:
    def __init__(self, source_path: pathlib.Path = LEGAL_TERMS_PATH):
        known_entities = self._load_known_entities(source_path)

        self.all_legal_terms: Set[Sequence[str]] = set(
            flatten(
                {name, *abbreviations} for name, abbreviations in known_entities.items()
            )
        )

        self.legal_terms_mapping: Mapping[
            Sequence[str], Sequence[Sequence[str]]
        ] = _prepare_dict_from_list_with_duplicates(
            itertools.chain.from_iterable(
                [(a, name) for a in abbreviations] + [(name, name)]
                for name, abbreviations in known_entities.items()
            )
        )

    def _load_known_entities(
        self, source_path: pathlib.Path
    ) -> Mapping[LegalTerm, Tuple[LegalTerm, ...]]:
        with source_path.open("rt", encoding="utf-8") as f:
            known_legal_entities = json.load(f)

        cleaned_legal_entities: Mapping[LegalTerm, Tuple[LegalTerm, ...]] = {
            tuple(divide(clear_name(name))): tuple(
                tuple(divide(n))
                for n in flatten(
                    self._legal_term_variants(clear_name(n)) for n in abbreviations
                )
            )
            for name, abbreviations in known_legal_entities.items()
        }

        if len(known_legal_entities) != len(cleaned_legal_entities):
            # TODO - discover which entries are duplicated
            raise Exception("Duplicated entries in known legal entities")

        return cleaned_legal_entities

    @staticmethod
    def _legal_term_variants(term: str) -> Set[str]:
        return {
            term,
            *(
                " ".join(w).strip()
                for w in itertools.product(
                    *[
                        (" ".join(t), "".join(t))
                        for t in term.split()
                    ]
                )
            ),
        }


LEGAL_TERMS = LegalTerms()
