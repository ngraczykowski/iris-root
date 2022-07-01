import pytest
from company_name.solution.scores_reduction import FeatureRule, ModelSolutionRule

from research.name_preconditions_rules import (
    AlphabetRuleConfig,
    InclusionRuleConfig,
    LengthRuleConfig,
)
from research.org_name_agent import OrgNameAgentService


@pytest.mark.skip
@pytest.mark.parametrize(
    "ap_names, wl_names, expected_recommendation",
    [
        (["Silent 8 Pte Ltd"], ["Silent Eight"], "MATCH"),
        (["Smiling Elephant"], ["Silent Eight"], "NO_MATCH"),
    ],
)
def test_org_name_default_rules(ap_names, wl_names, expected_recommendation):
    org_name_service = OrgNameAgentService()
    result = org_name_service.resolve(ap_names, wl_names)
    assert result.solution == expected_recommendation


@pytest.mark.skip
@pytest.mark.parametrize(
    "change_type, change_value, ap_names, wl_names, expected_recommendation",
    [
        ("feature_rules", None, ["Hewlett and Packard"], ["HP"], "MATCH"),
        (
            "feature_rules",
            [FeatureRule(feature="abbreviation", threshold=0.9, solution="NO_MATCH")],
            ["Hewlett and Packard"],
            ["HP"],
            "NO_MATCH",
        ),
    ],
)
def test_org_name_feature_rules_change(
    change_type, change_value, ap_names, wl_names, expected_recommendation
):
    org_name_service = OrgNameAgentService()
    args = {change_type: change_value}
    change_result = org_name_service.change_config(**args)
    assert change_result.status == 1  # means OK
    result = org_name_service.resolve(ap_names, wl_names)
    assert result.solution == expected_recommendation


@pytest.mark.skip
@pytest.mark.parametrize(
    "change_type, change_value, ap_names, wl_names, expected_recommendation",
    [
        (
            "model_solution_rules",
            [
                ModelSolutionRule(solution="NO_MATCH", threshold=0.9, label="NO_MATCH"),
                ModelSolutionRule(solution="MATCH", threshold=0, label="MATCH"),
            ],
            ["Lenovo Computers Poland Inc."],
            ["Legion Computers Poland Inc"],
            "MATCH",
        ),
        (
            "model_solution_rules",
            [
                ModelSolutionRule(solution="NO_MATCH", threshold=0.8, label="NO_MATCH"),
                ModelSolutionRule(solution="MATCH", threshold=0.2, label="MATCH"),
            ],
            ["Lenovo Computers Poland Inc."],
            ["Legion Computers Poland Inc"],
            "NO_MATCH",
        ),
    ],
)
def test_org_name_model_rules_change(
    change_type, change_value, ap_names, wl_names, expected_recommendation
):
    org_name_service = OrgNameAgentService()
    args = {change_type: change_value}
    change_result = org_name_service.change_config(**args)
    assert change_result.status == 1  # means OK

    result = org_name_service.resolve(ap_names, wl_names)
    assert result.solution == expected_recommendation


@pytest.mark.skip
@pytest.mark.parametrize(
    "change_type, change_value, expected_recommendation",
    [
        (
            "name_alphabet_rule",
            None,
            "NO_MATCH",
        ),
        (
            "name_alphabet_rule",
            AlphabetRuleConfig(min_acceptable_fraction=0.95, acceptable_alphabets=["LATIN"]),
            "INCONCLUSIVE",
        ),
        (
            "name_inclusion_rule",
            InclusionRuleConfig(without_part=[], without_token=["world"]),
            "INCONCLUSIVE",
        ),
        (
            "name_length_rule",
            LengthRuleConfig(max_length=40, max_unique_tokens=4),
            "INCONCLUSIVE",
        ),
    ],
)
def test_org_name_name_preconditions_change(change_type, change_value, expected_recommendation):
    ap_names = ["Hello World !!!"]
    wl_names = ["Some Very Long Name Here"]
    org_name_service = OrgNameAgentService()
    args = {change_type: change_value}
    change_result = org_name_service.change_config(**args)
    assert change_result.status == 1  # means OK

    result = org_name_service.resolve(ap_names, wl_names)
    assert result.solution == expected_recommendation


@pytest.mark.skip
@pytest.mark.parametrize(
    "change_type, change_value, ap_names, wl_names, expected_recommendation",
    [
        (
            "model_path",
            "model/tsaas-logistic-regression-2021.07.12.bin",
            ["Abstract Company"],
            ["Example Company"],
            "NO_MATCH",
        ),
        (
            "model_path",
            "model/random_forest_model_prod_22_12_21.bin",
            ["Abstract Company"],
            ["Example Company"],
            "MATCH",
        ),
    ],
)
def test_org_name_model_change(
    change_type, change_value, ap_names, wl_names, expected_recommendation
):
    org_name_service = OrgNameAgentService()
    args = {change_type: change_value}
    change_result = org_name_service.change_config(**args)
    assert change_result.status == 1  # means OK

    result = org_name_service.resolve(ap_names, wl_names)
    assert result.solution == expected_recommendation
