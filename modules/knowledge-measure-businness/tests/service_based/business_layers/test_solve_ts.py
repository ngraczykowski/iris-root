import pytest
import yaml

from business_layer.business_layer_ts import BusinessLayerTS
from business_layer.errors import PolicyStepsError, SolveInputDataError

with open("business_layer/config_ts.yaml", "r") as cfg_file:
    CONFIG_PARAMETERS_TS = yaml.safe_load(cfg_file)

POLICY_STEPS_TS = [
    {
        "decision": "PTP",
        "conditions": [{"geo_location": ["CITY_MATCH"]}],
    },
    {"decision": "PTP", "conditions": [{"org_name_name": ["MATCH"]}]},
    {
        "decision": "FP",
        "conditions": [
            {
                "geo_location": ["COUNTRY_NO_MATCH", "CITY_NO_MATCH"],
                "org_name_name": ["NO_MATCH"],
            },
        ],
    },
]


@pytest.mark.parametrize(
    "test_input, decision, comment",
    [
        (
            {
                "msg": "[ORIGINATOR     ] IT36701908273410 AC 121140399 BANK OF"
                " TIANJIN CO LTD NO.15 YOU YI ROAD,HE XI DISTRICT",
                "wl_name": "another company",
                "wl_location": "COLOMBIA",
                "wl_ssn": "123",
            },
            "FP",
            "S8 recommended action: False Positive\n"
            "Alerted Party's location (TIANJIN) does not match "
            "Watchlist Party's location (COLOMBIA)\n"
            "Alerted Party's name (AC BANK | BANK OF TIANJIN CO LTD "
            "| IT AC BANK | OF TIANJIN CO LTD)"
            " does not match Watchlist Party's name (another company)",
        ),
        (
            {
                "msg": "Los Angeles",
                "wl_name": "Hollywood",
                "wl_location": "Los Angeles, USA",
                "wl ssn": "00-000",
            },
            "PTP",
            "S8 recommended action: Potential True Positive\n"
            "Alerted Party's location (Los Angeles) matches "
            "Watchlist Party's location (Los Angeles)",
        ),
        (
            {
                "msg": "Some text about the Procter & Gamble Company",
                "wl_name": "P&G Comp.",
                "wl_location": "USA",
                "wl ssn": "007",
            },
            "PTP",
            "S8 recommended action: Potential True Positive\n"
            "Alerted Party's name (the Procter & Gamble Company) "
            "matches Watchlist Party's name (P&G Comp.)",
        ),
        (
            {
                "msg": "London, United Kingdom",
                "wl_name": "Some Manufacture",
                "wl_location": "Manchester, UK",
                "wl ssn": "007",
            },
            "FP",
            "S8 recommended action: False Positive\n"
            "Alerted Party's location (London) does not match "
            "Watchlist Party's location (Manchester)\n"
            "Alerted Party's name (London, United Kingdom) does not match "
            "Watchlist Party's name (Some Manufacture)",
        ),
        (
            {
                "msg": "New York",
                "wl_name": "Some Name",
                "wl_location": "NY, United States of America",
                "wl ssn": "456",
            },
            "MI",  # as it is STATE_MATCH, not CITY_MATCH
            "S8 recommended action: Manual Investigation",
        ),
        (
            {
                "msg": "New York City",
                "wl_name": "Empire State Building",
                "wl_location": "NYC",
                "wl ssn": "456",
            },
            "MI",  # both are cities, but 3 chars is too few - inconclusive
            "S8 recommended action: Manual Investigation",
        ),
    ],
)
def test_solve_ts(test_input, decision, comment):
    buss_layers_ts = BusinessLayerTS(config_parameters=CONFIG_PARAMETERS_TS)
    solved = buss_layers_ts.solve_hit(data=test_input, policy_steps=POLICY_STEPS_TS)
    assert solved.decision == decision
    assert solved.comment == comment


@pytest.mark.parametrize(
    "test_input, policy_steps, expected_exception",
    [
        ({}, POLICY_STEPS_TS, SolveInputDataError),
        (
            {
                "msg": "123",
                "SOME_RANDOM_KEY": "",
            },
            POLICY_STEPS_TS,
            SolveInputDataError,
        ),
        (
            {},
            [{"decision": "NOT_EXISTING_TYPE", "conditions": [{"": []}]}],
            PolicyStepsError,
        ),
        (
            {},
            [{"decision": "PTP", "conditions": [{"NOT_EXISTING_FEATURE": []}]}],
            PolicyStepsError,
        ),
    ],
)
def test_errors(test_input, policy_steps, expected_exception):
    business_layer = BusinessLayerTS(config_parameters=CONFIG_PARAMETERS_TS)
    with pytest.raises(expected_exception):
        business_layer.solve_hit(data=test_input, policy_steps=policy_steps)
