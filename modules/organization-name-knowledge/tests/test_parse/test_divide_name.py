from typing import Tuple

import pytest

from organization_name_knowledge.utils.text import divide, remove_split_chars


@pytest.mark.parametrize(
    ("name", "expected"),
    (
        (
            "GANSU TIANSHUI HAILIIN IMPORT AND EXPORT CO LTD",
            ("GANSU", "TIANSHUI", "HAILIIN", "IMPORT", "AND", "EXPORT", "CO", "LTD"),
        ),
        (
            " SAMA     NAJD OFFICE  ",
            ("SAMA", "NAJD", "OFFICE"),
        ),
        (
            "SAMA\nNAJD OFFICE",
            ("SAMA", "NAJD", "OFFICE"),
        ),
        (
            "SAMA\t\tNAJD OFFICE\t",
            ("SAMA", "NAJD", "OFFICE"),
        ),
        (
            "SAMA NAJD\t\n\t\rOFFICE",
            ("SAMA", "NAJD", "OFFICE"),
        ),
    ),
)
def test_divide_by_whitespaces(name: str, expected: Tuple[str, ...]):
    assert divide(name) == expected


@pytest.mark.parametrize(
    ("name", "expected"),
    (
        (
            "SHAHIN,TRADING,FZE",
            ("SHAHIN", "TRADING", "FZE"),
        ),
        (
            "SHAHIN/TRADING\\   FZE",
            ("SHAHIN", "TRADING", "FZE"),
        ),
        (
            "SHAHIN- TRADING -FZE",
            ("SHAHIN", "TRADING", "FZE"),
        ),
    ),
)
def test_divide_by_split_chars(name: str, expected: Tuple[str, ...]):
    assert divide(name) == expected


@pytest.mark.parametrize(
    ("name", "expected"),
    (
        (
            "PROTEUS SHIPPING PTE. LTD.",
            ("PROTEUS", "SHIPPING", "PTE.", "LTD."),
        ),
        (
            "PROTEUS.SHIPPING.PTE.LTD.",
            ("PROTEUS.", "SHIPPING.", "PTE.", "LTD."),
        ),
        (
            "PROTEUS SHIPPING PTE.CO.",
            ("PROTEUS", "SHIPPING", "PTE.", "CO."),
        ),
        (
            "PROTEUS.SHIPPING PTE.CO.LTD.",
            ("PROTEUS.", "SHIPPING", "PTE.", "CO.", "LTD."),
        ),
        (
            "I.P.C",
            ("I.P.C",),
        ),
        (
            "D.B.INTERNATIONAL ",
            ("D.B.", "INTERNATIONAL"),
        ),
    ),
)
def test_divide_by_dots(name: str, expected: Tuple[str, ...]):
    assert divide(name) == expected


@pytest.mark.parametrize(
    ("name", "expected"),
    (
        ("enkeltperson.foretak", "enkeltpersonforetak"),
        ("S/A", "SA"),
        ("G.m.b.H", "G.m.b.H"),
    ),
)
def test_remove_split_chars(name: str, expected: str):
    assert remove_split_chars(name) == expected
