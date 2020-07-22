package com.silenteight.sens.webapp.backend.configuration.solution;

import com.silenteight.sens.webapp.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.webapp.common.testing.rest.testwithrole.TestWithRole;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.util.List;

import static com.silenteight.sens.webapp.backend.configuration.solution.ConfigurationSolutionRestControllerTest.ConfigurationSolutionRestControllerFixtures.*;
import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.*;
import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Import({ ConfigurationSolutionRestController.class })
class ConfigurationSolutionRestControllerTest extends BaseRestControllerTest {

  @MockBean
  private SolutionsQuery solutionsQuery;

  @TestWithRole(role = BUSINESS_OPERATOR)
  void its200WithCorrectBody_whenFound() {
    given(solutionsQuery.list())
        .willReturn(
            List.of(
                NO_DECISION_SOLUTION,
                FALSE_POSITIVE_SOLUTION,
                HINTED_FALSE_POSITIVE_SOLUTION,
                POTENTIAL_TRUE_POSITIVE_SOLUTION,
                HINTED_POTENTIAL_TRUE_POSITIVE_SOLUTION));

    get(mappingForSolutions())
        .statusCode(OK.value())
        .body("size()", is(5))
        .body("[0]", equalTo(NO_DECISION_SOLUTION))
        .body("[1]", equalTo(FALSE_POSITIVE_SOLUTION))
        .body("[2]", equalTo(HINTED_FALSE_POSITIVE_SOLUTION))
        .body("[3]", equalTo(POTENTIAL_TRUE_POSITIVE_SOLUTION))
        .body("[4]", equalTo(HINTED_POTENTIAL_TRUE_POSITIVE_SOLUTION));
  }

  @TestWithRole(roles = { ADMIN, ANALYST, AUDITOR, APPROVER })
  void its403_whenNotPermittedRole() {
    get(mappingForSolutions()).statusCode(FORBIDDEN.value());
  }

  private String mappingForSolutions() {
    return format("/configuration/solutions");
  }

  static class ConfigurationSolutionRestControllerFixtures {

    static final String NO_DECISION_SOLUTION = "NO_DECISION";
    static final String FALSE_POSITIVE_SOLUTION = "FALSE_POSITIVE";
    static final String HINTED_FALSE_POSITIVE_SOLUTION = "HINTED_FALSE_POSITIVE";
    static final String POTENTIAL_TRUE_POSITIVE_SOLUTION = "POTENTIAL_TRUE_POSITIVE";
    static final String HINTED_POTENTIAL_TRUE_POSITIVE_SOLUTION = "HINTED_POTENTIAL_TRUE_POSITIVE";
  }
}
