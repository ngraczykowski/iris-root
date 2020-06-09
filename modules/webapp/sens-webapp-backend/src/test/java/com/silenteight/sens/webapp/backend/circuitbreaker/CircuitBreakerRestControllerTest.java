package com.silenteight.sens.webapp.backend.circuitbreaker;

import com.silenteight.sens.webapp.backend.config.exception.GenericExceptionControllerAdvice;
import com.silenteight.sens.webapp.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.webapp.common.testing.rest.testwithrole.TestWithRole;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.util.List;

import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.ANALYST;
import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.AUDITOR;
import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.BUSINESS_OPERATOR;
import static java.time.Instant.now;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Import({ CircuitBreakerRestController.class, GenericExceptionControllerAdvice.class })
class CircuitBreakerRestControllerTest extends BaseRestControllerTest {

  private static final String DISCREPANT_BRANCHES_URL = "/discrepant-branches";
  @MockBean
  private DiscrepancyCircuitBreakerQuery query;

  @TestWithRole(roles = { BUSINESS_OPERATOR })
  void its200WithEmptyListWhenNoDiscrepantBranches() {
    given(query.listDiscrepantBranches()).willReturn(emptyList());

    get(DISCREPANT_BRANCHES_URL)
        .contentType(anything())
        .statusCode(OK.value())
        .body("size()", is(0));
  }

  @TestWithRole(roles = { BUSINESS_OPERATOR })
  void its200WithCorrectBody_whenFound() {
    given(query.listDiscrepantBranches()).willReturn(List.of(
        new DiscrepantBranchDto(new ReasoningBranchIdDto(1L, 12L), now()),
        new DiscrepantBranchDto(new ReasoningBranchIdDto(4L, 15L), now())));

    get(DISCREPANT_BRANCHES_URL)
        .contentType(anything())
        .statusCode(OK.value())
        .body("[0].branchId.decisionTreeId", is(1))
        .body("[0].branchId.featureVectorId", is(12))
        .body("[1].branchId.decisionTreeId", is(4))
        .body("[1].branchId.featureVectorId", is(15));
  }

  @TestWithRole(roles = { ANALYST, AUDITOR })
  void its403_whenNotPermittedRole() {
    get(DISCREPANT_BRANCHES_URL).statusCode(FORBIDDEN.value());
  }
}

