from company_name.names.countries import COUNTRIES
from company_name.names.name_information import NameSequence
from .score import Score


def country_score(first_countries: NameSequence, second_countries: NameSequence) -> Score:
    first_country_ids, second_country_ids = (
        COUNTRIES.get_countries_ids(first_countries),
        COUNTRIES.get_countries_ids(second_countries),
    )

    if not first_country_ids or not second_country_ids:
        value = 0.5
    else:
        value = float(bool(first_country_ids.intersection(second_country_ids)))

    return Score(
        value=value,
        compared=(first_countries.original_tuple, second_countries.original_tuple)
    )
