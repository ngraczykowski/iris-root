import json

from company_name import CompanyNameAgent, CompanyNameEncoder


def test_result_when_no_data():
    result = CompanyNameAgent().resolve((), ())
    dumped = json.dumps(result, cls=CompanyNameEncoder)
    assert json.loads(dumped) == {"solution": "NO_DATA", "reason": {"partials": []}}


def test_single_no_match_result():
    result = CompanyNameAgent().resolve(("google",), ("facebook",))
    print(result)
    dumped = json.dumps(result, cls=CompanyNameEncoder)
    loaded = json.loads(dumped)

    assert loaded["solution"] == "NO_MATCH"
    assert len(loaded["reason"]["partials"]) == 1
    assert loaded["reason"]["partials"][0]["names"] == ["google", "facebook"]
    assert loaded["reason"]["partials"][0]["solution"] == "NO_MATCH"


def test_match_result():
    result = CompanyNameAgent().resolve(("google", "facebook"), ("facebook",))
    print(result)
    dumped = json.dumps(result, cls=CompanyNameEncoder)
    loaded = json.loads(dumped)

    assert loaded["solution"] == "MATCH"
    assert len(loaded["reason"]["partials"]) == 2

    assert loaded["reason"]["partials"][0]["names"] == ["facebook", "facebook"]
    assert loaded["reason"]["partials"][0]["solution"] == "MATCH"

    assert loaded["reason"]["partials"][1]["names"] == ["google", "facebook"]
    assert loaded["reason"]["partials"][1]["solution"] == "NO_MATCH"
