from importlib.resources import open_text
from typing import List

from organization_name_knowledge import resources
from organization_name_knowledge.names.name_information import NameInformation
from organization_name_knowledge.names.parse.parse import parse_name
from organization_name_knowledge.utils.text import clear_freetext
from organization_name_knowledge.utils.variants import get_substrings_from_consecutive_tokens

with open_text(resources, "sp_500.txt") as file:
    SP_500_COMPANIES = {name.lower() for name in file.read().splitlines()}


def get_from_known_organization_names(text: str) -> List[NameInformation]:
    tokens = clear_freetext(text).split()
    substrings = get_substrings_from_consecutive_tokens(
        tokens, min_tokens_number=1, max_tokens_number=5
    )
    names = [parse_name(substring) for substring in substrings if substring in SP_500_COMPANIES]
    return names
