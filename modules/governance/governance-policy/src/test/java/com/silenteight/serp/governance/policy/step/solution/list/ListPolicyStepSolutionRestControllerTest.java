package com.silenteight.serp.governance.policy.step.solution.list;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.serp.governance.common.web.exception.GenericExceptionControllerAdvice;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.util.List;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.OK;

@Import({ ListPolicyStepSolutionRestController.class, GenericExceptionControllerAdvice.class })
class ListPolicyStepSolutionRestControllerTest extends BaseRestControllerTest {

  private static final String LIST_SOLUTIONS_URL = "/v1/policies/steps/solutions";

  private final Fixtures fixtures = new Fixtures();

  @MockBean
  private PolicyStepSolutionQuery solutionsQuery;

  @TestWithRole(roles =
      { ADMINISTRATOR, ANALYST, AUDITOR, APPROVER, BUSINESS_OPERATOR, POLICY_MANAGER })
  void its200WithCorrectBody_whenFound() {
    given(solutionsQuery.list())
        .willReturn(
            List.of(
                fixtures.noDecisionSolution,
                fixtures.falsePositiveSolution,
                fixtures.hintedFalsePositiveSolution,
                fixtures.potentialTruePositiveSolution,
                fixtures.hintedPotentialTruePositiveSolution));

    get(LIST_SOLUTIONS_URL)
        .statusCode(OK.value())
        .body("size()", is(5))
        .body("[0]", equalTo(fixtures.noDecisionSolution))
        .body("[1]", equalTo(fixtures.falsePositiveSolution))
        .body("[2]", equalTo(fixtures.hintedFalsePositiveSolution))
        .body("[3]", equalTo(fixtures.potentialTruePositiveSolution))
        .body("[4]", equalTo(fixtures.hintedPotentialTruePositiveSolution));
  }

  private class Fixtures {

    String noDecisionSolution = "NO_DECISION";
    String falsePositiveSolution = "FALSE_POSITIVE";
    String hintedFalsePositiveSolution = "HINTED_FALSE_POSITIVE";
    String potentialTruePositiveSolution = "POTENTIAL_TRUE_POSITIVE";
    String hintedPotentialTruePositiveSolution = "HINTED_POTENTIAL_TRUE_POSITIVE";
  }
}
