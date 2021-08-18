from typing import Sequence, List

from company_name.names.name_information import NameInformation
from company_name.scores.abbreviation import get_abbreviation_score


def remove_members_abbreviations(names_list: Sequence[NameInformation]) -> List[NameInformation]:
    redundant_abbreviations = []
    for name in names_list:
        name_len = len(name.base.cleaned_name)
        names_shorter_than_name = [x for x in names_list if len(x.base.cleaned_name) < name_len]
        for shorter_name in names_shorter_than_name:
            abbreviation_score = get_abbreviation_score(name, shorter_name)
            if abbreviation_score.value > 0.75:
                redundant_abbreviations.append(shorter_name)
    names_without_redundant_abbreviations = [name for name in names_list if name not in redundant_abbreviations]
    return names_without_redundant_abbreviations
