import pytest

from organization_name_knowledge.freetext.parse import parse_freetext_names


@pytest.mark.parametrize(
    "freetext, expected_names",
    [
        (
            "Allianz Global Corporation\n",
            [
                {
                    "base": "Allianz",
                    "legal": "Corporation",
                    "source": "Allianz Global Corporation",
                },
                {"base": "Global", "legal": "Corporation", "source": "Global Corporation"},
            ],
        ),
        (
            "SIMECONDUCTOR MANUFACTURING\nINTERNATIONAL(SHANGHAI)CORPORATION\n19 ZHANGIJANG"
            " ROAD,PUDONG NEW \nAREA,SHANGHAI,CHINA 123456",
            [
                {
                    "base": "INTERNATIONAL SHANGHAI",
                    "legal": "CORPORATION",
                    "source": "INTERNATIONAL SHANGHAI CORPORATION",
                },
                {
                    "base": "MANUFACTURING INTERNATIONAL SHANGHAI",
                    "legal": "CORPORATION",
                    "source": "MANUFACTURING INTERNATIONAL SHANGHAI CORPORATION",
                },
                {"base": "SHANGHAI", "legal": "CORPORATION", "source": "SHANGHAI CORPORATION"},
            ],
        ),
        (
            "/1234\r\n1/LLC VTB DC\r\n2/ABC BLDG, 12, ABC RD\r\n3/RU/MOSCOW\n",
            [
                {"base": "LLC VTB", "legal": "LLC", "source": "LLC VTB"},
                {"base": "LLC VTB DC", "legal": "LLC", "source": "LLC VTB DC"},
            ],
        ),
        (
            "IT36701908273410\r\n1/OTHER COMPANY",
            [
                {"base": "1 OTHER", "legal": "COMPANY", "source": "1/other company"},
                {"base": "it 1 OTHER", "legal": "COMPANY", "source": "it 1/other company"},
                {"base": "other", "legal": "company", "source": "other company"},
            ],
        ),
        (
            "[ORIGINATOR     ] IT36701908273410 AC 121140399 BANK OF TIANJIN CO LTD NO.15 YOU YI ROAD,HE XI DISTRICT",
            [
                {"base": "ac bank", "legal": "", "source": "ac bank"},
                {"base": "bank of tianjin", "legal": "co ltd", "source": "bank of tianjin co ltd"},
                {"base": "it ac bank", "legal": "", "source": "it ac bank"},
                {"base": "tianjin", "legal": "co ltd", "source": "of tianjin co ltd"},
            ],
        ),
        (
            "123456 PR RETAIL LLC C/O BLACKPOINT PARTNERS, LLC 123 ABC ST SUITE 88 US 12345",
            [
                {"base": base, "legal": legal, "source": source}
                for base, legal, source in zip(
                    [
                        "blackpoint",
                        "c o blackpoint",
                        "o blackpoint",
                        "partners",
                        "pr retail",
                        "retail",
                    ],
                    ["llc", "c o", "llc", "llc", "llc c o", "llc c o"],
                    [
                        "blackpoint partners, llc",
                        "c/o blackpoint",
                        "o blackpoint partners, llc",
                        "partners, llc",
                        "pr retail llc c/o",
                        "retail llc c/o",
                    ],
                )
            ],
        ),
        (
            "ACME CO and Google Inc",
            [
                {"base": "ACME", "legal": "CO", "source": "acme co"},
                {"base": "Google", "legal": "Inc", "source": "google inc"},
            ],
        ),
        (
            "South Africa Organization",
            [
                {"base": "Africa", "legal": "Organization", "source": "Africa Organization"},
                {
                    "base": "South Africa",
                    "legal": "Organization",
                    "source": "South Africa Organization",
                },
            ],
        ),
    ],
)
def test_parse_freetext_names(freetext, expected_names):
    parsed_freetext = parse_freetext_names(
        freetext, base_tokens_upper_limit=3, name_tokens_lower_limit=2, name_tokens_upper_limit=7
    )
    assert len(parsed_freetext) == len(expected_names)
    for name_information, expected in zip(parsed_freetext, expected_names):
        assert name_information
        assert name_information.base.cleaned_name == expected["base"].lower()
        assert name_information.legal.cleaned_name == expected["legal"].lower()
        assert name_information.source.cleaned == expected["source"].lower()
