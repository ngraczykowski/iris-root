import pytest
import yaml

from business_layer.business_layer_ns import BusinessLayerNS
from business_layer.errors import PolicyStepsError, SolveInputDataError

with open("business_layer/config_ns.yaml", "r") as cfg_file:
    CONFIG_PARAMETERS_NS = yaml.safe_load(cfg_file)


POLICY_STEPS_NS = [
    {
        "decision": "PTP",
        "conditions": [
            {"example_sanctions_residency": ["TRUE"]},
            {"org_name_name": ["MATCH"], "geo_nationality": ["COUNTRY_MATCH"]},
        ],
    },
    {
        "decision": "FP",
        "conditions": [
            {"geo_nationality": ["COUNTRY_NO_MATCH"], "example_sanctions_residency": ["FALSE"]},
        ],
    },
]


@pytest.mark.parametrize(
    "test_input, decision, comment",
    [
        (
            {
                "ap_all_nationalities_aggregated": ["US"],
                "wl_all_nationalities_aggregated": ["CN"],
                "ap_all_residencies_aggregated": ["RU", "FR"],
                "wl_all_residencies_aggregated": ["FR"],
                "ap_all_pobs_aggregated": [],
                "wl_all_pobs_aggregated": ["CN"],
                "ap_all_names_aggregated": ["CPF BOARD"],
                "wl_all_names_aggregated": ["CPF"],
                "ap_all_passports_aggregated": ["123"],
                "wl_all_passports_aggregated": ["234"],
            },
            "PTP",
            "S8 recommended action: Potential True Positive\nAlerted Party's"
            " residency (RU) is on the sanctions list",
        ),
        (
            {
                "ap_all_nationalities_aggregated": ["US"],
                "wl_all_nationalities_aggregated": ["CN"],
                "ap_all_residencies_aggregated": ["PL", "FR"],
                "wl_all_residencies_aggregated": ["FR"],
                "ap_all_pobs_aggregated": [],
                "wl_all_pobs_aggregated": ["CN"],
                "ap_all_names_aggregated": ["CPF BOARD"],
                "wl_all_names_aggregated": ["CPF"],
                "ap_all_passports_aggregated": ["123"],
                "wl_all_passports_aggregated": ["234"],
            },
            "FP",
            "S8 recommended action: False Positive\nAlerted Party's nationality"
            " (US) does not match Watchlist Party's nationality (CN)",
        ),
        (
            {
                "ap_all_nationalities_aggregated": ["US"],
                "wl_all_nationalities_aggregated": ["US"],
                "ap_all_residencies_aggregated": ["PL", "FR"],
                "wl_all_residencies_aggregated": ["FR"],
                "ap_all_pobs_aggregated": [],
                "wl_all_pobs_aggregated": ["CN"],
                "ap_all_names_aggregated": ["CPF BOARD"],
                "wl_all_names_aggregated": ["CPF"],
                "ap_all_passports_aggregated": ["123"],
                "wl_all_passports_aggregated": ["234"],
            },
            "PTP",
            "S8 recommended action: Potential True Positive\nAlerted Party's name (CPF BOARD) "
            "matches Watchlist Party's name (CPF)\n"
            "Alerted Party's nationality (US) matches Watchlist Party's nationality (US)",
        ),
    ],
)
def test_solve_ns(test_input, decision, comment):
    buss_layers_ns = BusinessLayerNS(config_parameters=CONFIG_PARAMETERS_NS)
    solved = buss_layers_ns.solve_hit(data=test_input, policy_steps=POLICY_STEPS_NS)
    assert solved.decision == decision
    assert solved.comment == comment


@pytest.mark.parametrize(
    "test_input, policy_steps, expected_exception",
    [
        ({}, POLICY_STEPS_NS, SolveInputDataError),
        (
            {
                "ap_all_nationalities_aggregated": [],
                "SOME_RANDOM_KEY": [],
            },
            POLICY_STEPS_NS,
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
    business_layer = BusinessLayerNS(config_parameters=CONFIG_PARAMETERS_NS)
    with pytest.raises(expected_exception):
        business_layer.solve_hit(data=test_input, policy_steps=policy_steps)
