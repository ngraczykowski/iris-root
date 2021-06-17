import pytest

from company_name.compare import compare


@pytest.mark.parametrize(
    ("first", "second"),
    (
        ("google", "gogle"),
        ("John International", "Jon International"),
        ("Somofore", "Somophore"),
    ),
)
def test_phonetic_names(first, second):
    print(repr(first), repr(second), compare(first, second))
    scored = compare(first, second)
    assert scored["fuzzy_on_base"].value < 1
    assert scored["phonetics_on_base"].value == 1
