import itertools
import json
import pathlib
from typing import Any, Dict, List, NamedTuple, Sequence, Set, Tuple

import importlib_resources

from company_name.datasources.term_sources import TermSources
from company_name.utils.clear_name import clear_name, divide

LEGAL_TERMS_PATH = (
    importlib_resources.files("company_name") / "resources" / "legal_terms.json"
)


class LegalTermJsonEntity(NamedTuple):
    name: str
    abbreviations: Sequence[Tuple[str, ...]]
    meaning: Sequence[str]


class LegalTerm(NamedTuple):
    normalized: str
    meaning: Sequence[str]


class LegalTerms:
    def __init__(self, source_path: pathlib.Path = LEGAL_TERMS_PATH):
        known_entities: Set[LegalTermJsonEntity] = self._load_known_entities(
            source_path
        )

        self.legal_term_sources = TermSources(
            {
                abbreviation
                for entity in known_entities
                for abbreviation in entity.abbreviations
            }
        )

        self.source_to_legal_terms: Dict[Tuple[str, ...], List[LegalTerm]] = {}
        for entity in known_entities:
            legal_term = LegalTerm(normalized=entity.name, meaning=entity.meaning)
            for abbr in entity.abbreviations:
                if abbr in self.source_to_legal_terms:
                    self.source_to_legal_terms[abbr].append(legal_term)
                else:
                    self.source_to_legal_terms[abbr] = [legal_term]

    def _load_known_entities(
        self, source_path: pathlib.Path
    ) -> Set[LegalTermJsonEntity]:
        with source_path.open("rt", encoding="utf-8") as f:
            known_legal_entities = json.load(f)["legal_terms"]

        cleaned_legal_entities = {
            self._clean_entity(name, data)
            for name, data in known_legal_entities.items()
        }

        return cleaned_legal_entities

    @classmethod
    def _clean_entity(
        cls, entity_name: str, entity_data: Dict[str, Any]
    ) -> LegalTermJsonEntity:
        entity = LegalTermJsonEntity(
            name=entity_name,
            abbreviations=tuple(
                tuple(divide(n))
                for n in (
                    *itertools.chain.from_iterable(
                        cls._legal_term_variants(clear_name(n))
                        for n in entity_data.get("abbreviations", ())
                    ),
                    clear_name(entity_name),
                )
            ),
            meaning=tuple(entity_data.get("meaning", (entity_name.lower(),))),
        )
        return entity

    @staticmethod
    def _legal_term_variants(term: str) -> Set[str]:
        return {
            term,
            *(
                " ".join(w).strip()
                for w in itertools.product(
                    *[(" ".join(t), "".join(t)) for t in term.split()]
                )
            ),
        }


LEGAL_TERMS = LegalTerms()
