from typing import Tuple

from organization_name_knowledge.names.name_information import NameInformation
from organization_name_knowledge.names.parse import (
    create_tokens,
    extract_countries,
    extract_legal_terms,
    parse_name,
)


def parse(name: str) -> NameInformation:
    name_information = parse_name(name)
    return name_information


def get_countries(name: str) -> Tuple[str]:
    name_tokens = create_tokens(name)
    base, countries = extract_countries(name_tokens)
    return countries.original_tuple


def get_legal_terms(name: str) -> Tuple[str]:
    name_tokens = create_tokens(name)
    base, legal, other = extract_legal_terms(name_tokens)
    return legal.original_tuple
