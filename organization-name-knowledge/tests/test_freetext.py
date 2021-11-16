import pytest

from organization_name_knowledge import parse_freetext


@pytest.mark.parametrize(
    "freetext, expected_names",
    [
        (
            "Some Text about The Silent Eight PTE LTD founded years ago in Singapore",
            [{"base": "Silent Eight", "legal": "pte ltd"}],
        ),
        (
            "First Company Limited, Second sp. z. o. o.",
            [
                {"base": "First", "legal": "company limited"},
                {"base": "Second", "legal": "sp z o o"},
            ],
        ),
        (
            "Magic LTD and The Hogwarts Inc.",
            [{"base": "Hogwarts", "legal": "inc"}, {"base": "Magic", "legal": "ltd"}],
        ),
        (
            "The Hewlett and Packard Company",
            [
                {"base": "Packard", "legal": "company"},
                {"base": "Hewlett and Packard", "legal": "company"},
            ],
        ),
        (
            "ACME CO and Google Inc",
            [{"base": "ACME", "legal": "CO"}, {"base": "Google", "legal": "inc"}],
        ),
        (
            "Paramount Pictures LLC or Walt Disney Company",
            [
                {"base": "paramount pictures", "legal": "LLC"},
                {"base": "Walt Disney", "legal": "company"},
            ],
        ),
    ],
)
def test_freetext(freetext, expected_names):
    print(len(parse_freetext(freetext)))
    for name_information, expected in zip(parse_freetext(freetext), expected_names):
        assert name_information.base.cleaned_name == expected["base"].lower()
        assert name_information.legal.cleaned_name == expected["legal"].lower()
