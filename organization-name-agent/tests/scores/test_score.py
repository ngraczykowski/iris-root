import pytest

from company_name.scores.score import Score


def test_default_empty_score():
    assert Score().status == Score.ScoreStatus.NO_DATA
    assert Score().value == 0
    assert Score().compared == ((), ())


def test_score_comparison():
    a, b = Score(value=0.5), Score(value=0.7)
    assert a < b
    assert not a > b
    assert a != b


def test_score_comparison_when_empty():
    a, b = Score(), Score(value=0.7)
    assert a < b
    assert not a > b
    assert a != b


def test_score_comparison_when_same():
    a, b = Score(value=0.5), Score(value=0.5)
    assert not a < b
    assert not a > b
    assert a == b


def test_multiplication():
    a = Score(value=1)
    assert a * 1 == a
    assert a * 0.5 == Score(value=0.5)
    with pytest.raises(NotImplementedError):
        a * a


def test_multiplication_when_empty():
    a = Score()
    assert a * 1 == a
    assert a * 0.5 == a
