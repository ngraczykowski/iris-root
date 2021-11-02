import itertools
from typing import Mapping

from organization_name_knowledge.knowledge_base.term_sources import TermSources
from organization_name_knowledge.utils.clear_name import divide


class CommonTerms:
    def __init__(self, data, weak_words):
        terms = [
            [tuple(divide(source)) for source in (n if isinstance(n, list) else (n,))]
            for n in (*data, ["", *weak_words])
        ]

        self.terms = TermSources(set(itertools.chain.from_iterable(terms)))
        self.source_to_common_base: Mapping[str, str] = {
            " ".join(term): " ".join(terms_group[0])
            for terms_group in terms
            for term in terms_group
        }
