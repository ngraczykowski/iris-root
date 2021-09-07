from id_mismatch_agent.agent import identification_mismatch_agent


def test_search_code_mismatch_agent(logic_test_case):
    expected_solution, expected_reason = logic_test_case.expected_values

    # when
    result = identification_mismatch_agent(logic_test_case.input)

    # then
    assert result.solution == expected_solution
    assert result.reason == expected_reason
