from bank_identification_codes_agent.agent import BankIdentificationCodesAgent


def test_search_code_mismatch_agent(logic_test_case):
    expected_solution, expected_reason = logic_test_case.expected_values

    # when
    result = BankIdentificationCodesAgent().resolve(logic_test_case.input)

    # then
    assert result.solution == expected_solution
    assert result.reason == expected_reason
