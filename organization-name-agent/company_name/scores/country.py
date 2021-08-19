from company_name.knowledge_base import KnowledgeBase
from company_name.names.name_information import TokensSequence
from company_name.scores.score import Score


def get_country_score(first_countries: TokensSequence, second_countries: TokensSequence) -> Score:
    if not first_countries and not second_countries:
        return Score()

    first_country_ids, second_country_ids = (
        KnowledgeBase.countries.get_countries_ids(first_countries),
        KnowledgeBase.countries.get_countries_ids(second_countries),
    )
    value = float(bool(first_country_ids.intersection(second_country_ids)))

    return Score(
        value=value,
        compared=(first_countries.original_tuple, second_countries.original_tuple),
    )
