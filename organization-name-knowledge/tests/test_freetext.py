import pytest

from organization_name_knowledge import parse_freetext


@pytest.mark.parametrize(
    "freetext, expected_names",
    [
        (
            "Some Text about The Silent Eight PTE LTD founded years ago in Singapore",
            ["Silent Eight"],
        ),
        (
            "First Company Limited, Second sp. z. o. o.",
            ["First", "Second"],
        ),
        (
            "Magic LTD and The Hogwarts Inc.",
            ["Hogwarts", "Magic"],
        ),
        (
            "The Hewlett and Packard Company",
            ["Packard", "Hewlett and Packard"],
        ),
    ],
)
def test_freetext(freetext, expected_names):
    for name_information, expected in zip(parse_freetext(freetext), expected_names):
        assert name_information.base.cleaned_name == expected.lower()
