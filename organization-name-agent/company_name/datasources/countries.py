import collections
import json
import pathlib
from typing import Set

from company_name.utils.clear_name import clear_name, divide
from company_name.names.name_information import TokensSequence
from company_name.datasources.term_sources import TermSources

COUNTRIES_PATH = pathlib.Path(__file__).parent / "countries.json"


class Countries:
    def __init__(self, source_path: pathlib.Path = COUNTRIES_PATH):
        with source_path.open("rt", encoding="utf-8") as f:
            countries = json.load(f)

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


COUNTRIES = Countries()
