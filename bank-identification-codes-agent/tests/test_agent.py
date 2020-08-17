from idmismatchagent.agent import identification_mismatch_agent


def test_search_code_mismatch_agent(logic_test_case):
    expected_result, expected_reason = logic_test_case.expected_values

    # when
    actual_result, actual_reason = identification_mismatch_agent(logic_test_case.input)

    # then
    assert actual_result == expected_result
    assert actual_reason == expected_reason
