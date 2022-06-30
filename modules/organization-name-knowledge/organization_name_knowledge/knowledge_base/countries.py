import collections
from typing import List, Set

from organization_name_knowledge.knowledge_base.term_sources import TermSources
from organization_name_knowledge.names.tokens_sequence import TokensSequence
from organization_name_knowledge.utils.text import clear_name, divide


class Countries:
    def __init__(self, countries: List[List[str]]):
        self.mapping = collections.defaultdict(list)
        for i, country_names in enumerate(countries):
            for country_name in country_names:
                self.mapping[clear_name(country_name)].append(i)

        self.countries = TermSources({divide(name) for name in self.mapping})

    def get_countries_ids(self, countries: TokensSequence) -> Set[int]:
        return {
            country_id
            for country_name in countries
            for country_id in self.mapping.get(country_name.cleaned, [])
        }
