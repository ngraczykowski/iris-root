class ServerIsNotRunning(Exception):
    pass


TEST_CASES = [
    {
        "data": {"alerted_party_names": ["ABC"], "watchlist_party_names": ["ABCDE"]},
        "solution": "MATCH",
        "probability": 1,
        "results_number": 1,
    },
    {
        "data": {"alerted_party_names": ["ABC"], "watchlist_party_names": ["DEF"]},
        "solution": "NO_MATCH",
        "probability": 0.98,
        "results_number": 1,
    },
    {
        "data": {
            "alerted_party_names": ["HP", "JP Morgan", "SCB"],
            "watchlist_party_names": ["Silent Eight", "Hewlett & Packard"],
        },
        "solution": "MATCH",
        "probability": 0.8,
        "results_number": 6,
    },
    {
        "data": {"alerted_party_names": [], "watchlist_party_names": []},
        "solution": "NO_DATA",
        # no probability here as it is 'NO_DATA'
        "results_number": 0,
    },
]
