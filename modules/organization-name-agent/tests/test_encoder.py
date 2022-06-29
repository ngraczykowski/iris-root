import json

from company_name import CompanyNameAgent, CompanyNameEncoder


def test_result_when_no_data():
    result = CompanyNameAgent()._resolve((), ())
    dumped = json.dumps(result, cls=CompanyNameEncoder)
    assert json.loads(dumped) == {"solution": "NO_DATA", "reason": {"results": []}}


def test_single_no_match_result():
    result = CompanyNameAgent().resolve(("google",), ("facebook",))
    print(result)
    dumped = json.dumps(result, cls=CompanyNameEncoder)
    loaded = json.loads(dumped)

    assert loaded["solution"] == "NO_MATCH"
    assert len(loaded["reason"]["results"]) == 1
    assert loaded["reason"]["results"][0]["alerted_party_name"] == "google"
    assert loaded["reason"]["results"][0]["watchlist_party_name"] == "facebook"
    assert loaded["reason"]["results"][0]["solution"] == "NO_MATCH"


def test_match_result():
    result = CompanyNameAgent().resolve(("google", "facebook"), ("facebook",))
    print(result)
    dumped = json.dumps(result, cls=CompanyNameEncoder)
    loaded = json.loads(dumped)

    assert loaded["solution"] == "MATCH"
    assert len(loaded["reason"]["results"]) == 2

    assert loaded["reason"]["results"][0]["alerted_party_name"] == "facebook"
    assert loaded["reason"]["results"][0]["watchlist_party_name"] == "facebook"
    assert loaded["reason"]["results"][0]["solution"] == "MATCH"

    assert loaded["reason"]["results"][1]["alerted_party_name"] == "google"
    assert loaded["reason"]["results"][1]["watchlist_party_name"] == "facebook"
    assert loaded["reason"]["results"][1]["solution"] == "NO_MATCH"
