from company_name.names.name_information import NameInformation
from company_name.scores.score import Score


def _potential_subsidiary_parent(name: NameInformation) -> bool:
    return 'group' in name.name().cleaned_tuple


def potential_subsidiary_score(
    first: NameInformation, second: NameInformation
) -> Score:
    values = _potential_subsidiary_parent(first), _potential_subsidiary_parent(second)
    return Score(
        value=float(any(values)),
        compared=(
            (first.original,) if values[0] else (),
            (second.original,) if values[1] else (),
        ),
    )
