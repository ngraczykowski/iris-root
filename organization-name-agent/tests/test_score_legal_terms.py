import pytest

from company_name.compare import compare, Score


def _get_legal_terms_result(first_legal: str, second_legal: str) -> float:
    result = compare("AAAA " + first_legal, "AAAA " + second_legal)
    print(result)
    assert " ".join(result["legal_terms"].compared[0]) == first_legal.replace(",", "")
    assert " ".join(result["legal_terms"].compared[1]) == second_legal.replace(",", "")
    return result["legal_terms"].value


@pytest.mark.parametrize(
    ("first", "second", "value"),
    (
        ("A corp", "A corporation", 1),
        ("A corp", "A", 0),
        ("A", "A", 0),
    ),
)
def test_compare_legal_terms(first: str, second: str, value: float):
    print(repr(first), repr(second))
    result = compare(first, second)
    print(result)
    assert result["legal_terms"].value == value


@pytest.mark.parametrize(
    ("first", "second"),
    (
        ("ASML Holding", "ASML Holding N.V."),
        ("CDW Corporation", "CDW"),
        ("Charter Communications, Inc.", "Charter"),
        ("toyota motor finance", "TOYOTA MOTOR FINANCE (CHINA) COMPANY LIMITED"),
    ),
)
def test_compare_legal_on_one_side(first: str, second: str):
    print(repr(first), repr(second))
    result = compare(first, second)
    print(result)
    assert result["fuzzy_on_base"].value == 1

    assert result["legal_terms"].value == 0
    assert result["legal_terms"].compared
    assert result["legal_terms"].status in (
        Score.ScoreStatus.NO_ALERTED_PARTY_DATA,
        Score.ScoreStatus.NO_MATCHED_PARTY_DATA,
    )


@pytest.mark.parametrize(
    ("first", "second"),
    (
        ("AIRSTAL SP Z O O", "AIRSTAL sp. z o.o."),
        ("Nejdecká česárna vlny, a. s.", "Nejdecká česárna vlny AS"),
        ("BULLDOG SPORTS S A S", "BULLDOG SPORTS SAS"),
        ("COMPLEXICA PTY LTD", "COMPLEXICA PTY. LTD."),
        (
            "FLORAL MANUFACTURING GROUP COMPANY LIMITED",
            "FLORAL MANUFACTURING GROUP COMPANY LTD.",
        ),
        (
            "AGENSI PEKERJAAN PERTAMA SENDIRIAN BERHAD",
            "AGENSI PEKERJAAN PERTAMA SDN. BHD.",
        ),
    ),
)
def test_compare_same_legal_terms(first: str, second: str):
    result = compare(first, second)
    assert result["fuzzy"].value == 1
    assert result["legal_terms"].value == 1


@pytest.mark.parametrize(
    ("first_legal", "second_legal"),
    (("GmbH & Co KG", "GmbH"),),
)
def test_partial_legal_terms(first_legal: str, second_legal: str):
    assert _get_legal_terms_result(first_legal, second_legal) > 0


@pytest.mark.parametrize(
    ("first_legal", "second_legal"),
    (("Co., Ltd.", "Ltd Company"),),
)
def test_compare_multiple_legal_terms(first_legal: str, second_legal: str):
    assert _get_legal_terms_result(first_legal, second_legal) == 1


@pytest.mark.parametrize(
    ("first_legal", "second_legal"),
    (
        ("limited liability partnership", "limited liability limited partnership"),
        ("limited liability company", "limited liability partnership"),
    ),
)
def test_similar_legal(first_legal: str, second_legal: str):
    assert _get_legal_terms_result(first_legal, second_legal) >= 0.5


@pytest.mark.parametrize(
    ("first_legal", "second_legal"),
    (
        ("OOO", "limited liability company"),
        ("OAO", "public joint-stock company"),
        ("jednoosobowa działalność gospodarcza", "sole proprietorship"),
        ("SA", "plc"),
        ("bhd", "plc"),
        ("AG", "plc"),
    ),
)
def test_same_legal_from_different_countries(first_legal: str, second_legal: str):
    assert _get_legal_terms_result(first_legal, second_legal) == 1


@pytest.mark.parametrize(
    ("first", "second"),
    (
        (
            "DIAMOND POCKET BOOKS PRIVATE LIMITED",
            "DIAMOND POCKET BOOKS (PRIVATE LIMITED)",
        ),
        ("HUNZA SUGAR MILLS (PVT.) LIMITED", "HUNZA SUGAR MILLS PRIVATE LIMITED"),
    ),
)
def test_legal_in_parenthesis(first, second):
    result = compare(first, second)
    assert result["fuzzy"].value == 1
    assert result["legal_terms"].value == 1
