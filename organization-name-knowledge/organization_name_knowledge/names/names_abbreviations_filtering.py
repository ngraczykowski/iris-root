from typing import List, Sequence

from organization_name_knowledge.names.name_information import NameInformation
from organization_name_knowledge.scores.abbreviation import get_abbreviation_score


def remove_redundant_abbreviations(
    names: Sequence[NameInformation],
) -> List[NameInformation]:
    redundant_abbreviations = []
    for name in names:
        name_len = len(name.base.cleaned_name)
        for shorter_name in filter(lambda x: len(x.base.cleaned_name) < name_len, names):
            abbreviation_score = get_abbreviation_score(name, shorter_name)
            if abbreviation_score.value == 1:
                redundant_abbreviations.append(shorter_name)
    names_without_redundant_abbreviations = [
        name for name in names if name not in redundant_abbreviations
    ]
    return names_without_redundant_abbreviations
