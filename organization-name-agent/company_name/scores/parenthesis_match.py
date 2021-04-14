from company_name.names.name_information import NameInformation
from .score import Score


def parenthesis_score(first: NameInformation, second: NameInformation) -> Score:
    second_base_appeared, first_base_appeared = (
        second.base.cleaned_name in first.parenthesis.cleaned_tuple,
        first.base.cleaned_name in second.parenthesis.cleaned_tuple,
    )
    return Score(
        value=float(first_base_appeared or second_base_appeared),
        compared=(
            (second.base.original_name, ) if second_base_appeared else (),
            (first.base.original_name, ) if first_base_appeared else (),
        ),
    )
