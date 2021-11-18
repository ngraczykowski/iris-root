from typing import List

from organization_name_knowledge.names.name_information import NameInformation


def filter_duplicate_bases(names: List[NameInformation]):
    bases = []
    for name in names:
        if name.base in bases:
            names.remove(name)
        else:
            bases.append(name.base)


def get_long_names_substrings(names: List[NameInformation]) -> List[str]:
    names_from_substrings = []
    for name_information in names:
        base_tokens_number = len(name_information.base)
        if base_tokens_number >= 2:
            _add_names_from_name_substrings(
                base_tokens_number, name_information, names_from_substrings
            )
    return names_from_substrings


def _add_names_from_name_substrings(
    tokens_number: int, name_information: NameInformation, names_from_substrings: List[str]
):
    for index in range(1, tokens_number):
        replaced = name_information.source.original
        for token_from_first_text_part in name_information.base.original_tuple[:index]:
            replaced = replaced.replace(token_from_first_text_part + " ", "")
        names_from_substrings.append(replaced.strip())
