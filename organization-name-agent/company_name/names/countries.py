import collections
import itertools
import json
import pathlib
from typing import Set

from company_name.utils.clear_name import clear_name
from .name_information import NameSequence

COUNTRIES_PATH = pathlib.Path(__file__).parent / "countries.json"


class Countries:
    def __init__(self, source_path: pathlib.Path = COUNTRIES_PATH):
        with source_path.open("rt", encoding="utf-8") as f:
            self.countries = json.load(f)

        self.mapping = collections.defaultdict(list)
        for i, country_names in enumerate(self.countries):
            for country_name in country_names:
                name = clear_name(country_name)
                self.mapping[name].append(i)
                if len(name) < 4:
                    self.mapping[".".join(name) + "."].append(i)

    def get_countries_ids(self, countries: NameSequence) -> Set[int]:
        return set(
            itertools.chain.from_iterable(self.mapping.get(c.cleaned, []) for c in countries)
        )


COUNTRIES = Countries()
