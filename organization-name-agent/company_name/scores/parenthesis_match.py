from company_name.names.name_information import NameInformation
from company_name.scores.score import Score


def parenthesis_score(first: NameInformation, second: NameInformation) -> Score:
    if not first.parenthesis and not second.parenthesis:
        return Score()

    second_base_appeared, first_base_appeared = (
        second.base.cleaned_name in (n.name().cleaned_name for n in first.parenthesis),
        first.base.cleaned_name in (n.name().cleaned_name for n in second.parenthesis),
    )
    return Score(
        status=Score.ScoreStatus.OK,
        value=float(first_base_appeared or second_base_appeared),
        compared=(
            (second.base.original_name, ) if second_base_appeared else (),
            (first.base.original_name, ) if first_base_appeared else (),
        ),
    )
