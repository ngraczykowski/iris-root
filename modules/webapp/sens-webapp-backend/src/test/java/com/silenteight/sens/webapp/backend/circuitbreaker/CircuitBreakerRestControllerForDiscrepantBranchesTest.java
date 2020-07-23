package com.silenteight.sens.webapp.backend.circuitbreaker;

import com.silenteight.sens.webapp.backend.config.exception.GenericExceptionControllerAdvice;
import com.silenteight.sens.webapp.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.webapp.common.testing.rest.testwithrole.TestWithRole;

import org.junit.jupiter.api.Nested;
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
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Import({
    CircuitBreakerRestController.class,
    CircuitBreakerRestControllerAdvice.class,
    GenericExceptionControllerAdvice.class })
class CircuitBreakerRestControllerForDiscrepantBranchesTest extends BaseRestControllerTest {

  private static final String DISCREPANT_BRANCHES_URL_TEMPLATE =
      "/discrepant-branches?discrepancyStatuses=ACTIVE";
  private static final String DISCREPANT_BRANCHES_ARCHIVE_URL_TEMPLATE =
      "/discrepant-branches?discrepancyStatuses=ARCHIVED";
  private static final String DISCREPANT_BRANCHES_WITHOUT_PARAM_URL_TEMPLATE =
      "/discrepant-branches";
  private static final String DISCREPANT_BRANCHES_WITH_MALFORMED_PARAM_URL_TEMPLATE =
      "/discrepant-branches?discrepancyStatuses=undefined";

  @MockBean
  private DiscrepancyCircuitBreakerQuery query;

  @MockBean
  private ArchiveDiscrepanciesUseCase archiveUseCase;

  @Nested
  class DiscrepantBranchesList {

    @TestWithRole(roles = { BUSINESS_OPERATOR })
    void its200WithEmptyListWhenNoBranchesWithDiscrepancies() {
      given(query.listBranchesWithDiscrepancies()).willReturn(emptyList());

      get(DISCREPANT_BRANCHES_URL_TEMPLATE)
          .contentType(anything())
          .statusCode(OK.value())
          .body("size()", is(0));
    }

    @TestWithRole(roles = { BUSINESS_OPERATOR })
    void its200WithCorrectBody_whenFound() {
      long decisionTreeId1 = 1L;
      long featureVectorId1 = 12L;
      long decisionTreeId2 = 4L;
      long featureVectorId2 = 15L;
      given(query.listBranchesWithDiscrepancies()).willReturn(List.of(
          new DiscrepantBranchDto(
              new ReasoningBranchIdDto(decisionTreeId1, featureVectorId1), now()),
          new DiscrepantBranchDto(
              new ReasoningBranchIdDto(decisionTreeId2, featureVectorId2), now())));

      get(DISCREPANT_BRANCHES_URL_TEMPLATE)
          .contentType(anything())
          .statusCode(OK.value())
          .body("[0].branchId.decisionTreeId", is((int) decisionTreeId1))
          .body("[0].branchId.featureVectorId", is((int) featureVectorId1))
          .body("[1].branchId.decisionTreeId", is((int) decisionTreeId2))
          .body("[1].branchId.featureVectorId", is((int) featureVectorId2));
    }

    @TestWithRole(roles = { ANALYST, AUDITOR })
    void its403_whenNotPermittedRole() {
      get(DISCREPANT_BRANCHES_URL_TEMPLATE).statusCode(FORBIDDEN.value());
    }

    @TestWithRole(roles = { BUSINESS_OPERATOR })
    void its400_whenRequiredParamIsMissing() {
      get(DISCREPANT_BRANCHES_WITHOUT_PARAM_URL_TEMPLATE).statusCode(BAD_REQUEST.value());
    }

    @TestWithRole(roles = { BUSINESS_OPERATOR })
    void its400_whenRequiredParamIsMalformed() {
      get(DISCREPANT_BRANCHES_WITH_MALFORMED_PARAM_URL_TEMPLATE).statusCode(BAD_REQUEST.value());
    }
  }

  @Nested
  class BranchListWithArchivedDiscrepancies {

    @TestWithRole(roles = { BUSINESS_OPERATOR })
    void its200WithEmptyListWhenNoBranchesWithArchivedDiscrepancies() {
      given(query.listBranchesWithArchivedDiscrepancies()).willReturn(emptyList());

      get(DISCREPANT_BRANCHES_ARCHIVE_URL_TEMPLATE)
          .contentType(anything())
          .statusCode(OK.value())
          .body("size()", is(0));
    }

    @TestWithRole(roles = { BUSINESS_OPERATOR })
    void its200WithCorrectBody_whenFound() {
      long decisionTreeId1 = 1L;
      long featureVectorId1 = 12L;
      long decisionTreeId2 = 4L;
      long featureVectorId2 = 15L;
      given(query.listBranchesWithArchivedDiscrepancies()).willReturn(List.of(
          new DiscrepantBranchDto(
              new ReasoningBranchIdDto(decisionTreeId1, featureVectorId1), now()),
          new DiscrepantBranchDto(
              new ReasoningBranchIdDto(decisionTreeId2, featureVectorId2), now())));

      get(DISCREPANT_BRANCHES_ARCHIVE_URL_TEMPLATE)
          .contentType(anything())
          .statusCode(OK.value())
          .body("[0].branchId.decisionTreeId", is((int) decisionTreeId1))
          .body("[0].branchId.featureVectorId", is((int) featureVectorId1))
          .body("[1].branchId.decisionTreeId", is((int) decisionTreeId2))
          .body("[1].branchId.featureVectorId", is((int) featureVectorId2));
    }

    @TestWithRole(roles = { ANALYST, AUDITOR })
    void its403_whenNotPermittedRole() {
      get(DISCREPANT_BRANCHES_ARCHIVE_URL_TEMPLATE).statusCode(FORBIDDEN.value());
    }
  }
}

