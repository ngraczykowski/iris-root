from typing import Sequence

import pytest

from company_name import CompanyNameAgent, Solution


@pytest.mark.parametrize(
    "shortcut, full",
    [
        ("SCB", "Standard Chartered Bank"),
    ],
)
def test_shortcut_recognition(shortcut: str, full: str):
    shortcut_from_full = get_shortcut(full)  # to implement
    assert shortcut == shortcut_from_full


@pytest.mark.parametrize(
    "names_list, valid_term",
    [
        (("SCB", "Standard Chartered Bank"), "Siam Commercial Bank"),
    ],
)
def test_shortcut_removal(names_list: Sequence[str], valid_term: str):
    filtered_names_list = remove_shortcuts(names_list)  # to implement
    result = CompanyNameAgent().resolve(filtered_names_list, [valid_term])
    assert result.solution == Solution.NO_MATCH
