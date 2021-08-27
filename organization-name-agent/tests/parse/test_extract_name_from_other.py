import pytest

from company_name.names.parse.parse import parse_name


@pytest.mark.parametrize(
    "name, expected",
    [
        (
            "Fun Coding Limited Codersheaven Poland",
            {"combined": "Fun Coding Codersheaven", "legal": "Limited", "countries": "Poland"},
        ),
        (
            "OSANG HEAL THCARE CO LTD INFOPIA",
            {"combined": "OSANG HEAL THCARE INFOPIA", "legal": "CO LTD", "countries": ""},
         ),
        (
            "SCCM DYBNG AND PRINTING CO. ATO",
            {"combined": "SCCM DYBNG AND PRINTING ATO", "legal": "CO.", "countries": ""},
        ),
    ],
)
def test_name_from_other(name, expected):
    name_information = parse_name(name)
    assert name_information.combine_name_and_other().original_name == expected["combined"]
    assert name_information.legal.original_name == expected['legal']
    assert name_information.countries.original_name == expected['countries']
