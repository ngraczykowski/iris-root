from typing import List, Sequence

from organization_name_knowledge.names.name_information import NameInformation

from company_name.scores.abbreviation import get_abbreviation_score


def remove_redundant_abbreviations(names: Sequence[NameInformation]) -> List[NameInformation]:
    names_redundant_abbreviations = []
    for name in names:
        names_redundant_abbreviations.extend(get_redundant_abbreviations(name, names))

    names_without_redundant_abbreviations = [
        name for name in names if name not in names_redundant_abbreviations
    ]
    return names_without_redundant_abbreviations


def get_redundant_abbreviations(name: NameInformation, names: Sequence[NameInformation]):
    name_len = len(name.base.cleaned_name)
    shorter_names = filter(lambda x: len(x.base.cleaned_name) < name_len, names)

    redundant_abbreviations = [
        shorter_name
        for shorter_name in shorter_names
        if get_abbreviation_score(name, shorter_name).value == 1
    ]
    return redundant_abbreviations
