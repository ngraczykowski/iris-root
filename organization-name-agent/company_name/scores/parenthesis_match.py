from organization_name_knowledge.names.name_information import NameInformation

from company_name.scores.score import Score


def get_parenthesis_score(first: NameInformation, second: NameInformation) -> Score:
    if not first.parenthesis and not second.parenthesis:
        return Score()

    second_base_appeared, first_base_appeared = (
        second.base.cleaned_name in first.parenthesis.cleaned_name,
        first.base.cleaned_name in second.parenthesis.cleaned_name,
    )
    return Score(
        status=Score.ScoreStatus.OK,
        value=float(first_base_appeared or second_base_appeared),
        compared=(
            (second.base.original_name,) if second_base_appeared else (),
            (first.base.original_name,) if first_base_appeared else (),
        ),
    )
