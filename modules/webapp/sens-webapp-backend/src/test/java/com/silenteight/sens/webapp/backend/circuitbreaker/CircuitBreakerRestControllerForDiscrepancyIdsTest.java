package com.silenteight.sens.webapp.backend.circuitbreaker;

import com.silenteight.sens.webapp.backend.config.exception.GenericExceptionControllerAdvice;
import com.silenteight.sens.webapp.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.webapp.common.testing.rest.testwithrole.TestWithRole;

import org.junit.jupiter.api.Nested;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.util.List;

import static com.silenteight.sens.webapp.backend.circuitbreaker.DiscrepancyStatus.ACTIVE;
import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.ANALYST;
import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.AUDITOR;
import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.BUSINESS_OPERATOR;
import static java.lang.String.format;
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
class CircuitBreakerRestControllerForDiscrepancyIdsTest extends BaseRestControllerTest {

  private static final String DISCREPANCY_IDS_URL_TEMPLATE =
      "/discrepant-branches/%s/discrepancy-ids?discrepancyStatuses=%s";

  private static final String DISCREPANCY_IDS_WITHOUT_REQUIRED_PARAM_URL_TEMPLATE =
      "/discrepant-branches/%s/discrepancy-ids";

  @MockBean
  private DiscrepancyCircuitBreakerQuery query;

  @MockBean
  private ArchiveDiscrepanciesUseCase archiveUseCase;

  @Nested
  class DiscrepancyIdsList {

    @TestWithRole(roles = { BUSINESS_OPERATOR })
    void its200WithEmptyListWhenNoDiscrepancyIds() {
      long decisionTreeId = 1L;
      long featureVectorId = 2L;
      given(query.listDiscrepancyIds(
          new ReasoningBranchIdDto(decisionTreeId, featureVectorId))).willReturn(emptyList());

      get(discrepancyIdsUrlWith(decisionTreeId, featureVectorId))
          .contentType(anything())
          .statusCode(OK.value())
          .body("size()", is(0));
    }

    @TestWithRole(roles = { BUSINESS_OPERATOR })
    void its200WithCorrectBody_whenFound() {
      long decisionTreeId = 2L;
      long featureVectorId = 3L;
      given(query.listDiscrepancyIds(
          new ReasoningBranchIdDto(decisionTreeId, featureVectorId))).willReturn(List.of(22L, 33L));

      get(discrepancyIdsUrlWith(decisionTreeId, featureVectorId))
          .contentType(anything())
          .statusCode(OK.value())
          .body("size()", is(2))
          .body("[0]", is(22))
          .body("[1]", is(33));
    }

    @TestWithRole(roles = { BUSINESS_OPERATOR })
    void its400_whenBranchIdIncorrect() {
      get(discrepancyIdsUrlWith("abc"))
          .contentType(anything())
          .statusCode(BAD_REQUEST.value());

      get(discrepancyIdsUrlWith("abc-bcd"))
          .contentType(anything())
          .statusCode(BAD_REQUEST.value());
    }

    @TestWithRole(roles = { BUSINESS_OPERATOR })
    void its400_whenArchiveParamIsMissing() {
      get(format(DISCREPANCY_IDS_WITHOUT_REQUIRED_PARAM_URL_TEMPLATE, "1-1"))
          .contentType(anything())
          .statusCode(BAD_REQUEST.value());
    }

    private String discrepancyIdsUrlWith(
        long decisionTreeId, long featureVectorId) {
      return format(
          DISCREPANCY_IDS_URL_TEMPLATE,
          format("%d-%d", decisionTreeId, featureVectorId), ACTIVE);
    }

    private String discrepancyIdsUrlWith(String branchId) {
      return format(DISCREPANCY_IDS_URL_TEMPLATE, branchId, ACTIVE);
    }
  }

  @Nested
  class ArchivedDiscrepancyIdsList {

    @TestWithRole(roles = { BUSINESS_OPERATOR })
    void its200WithEmptyListWhenNoDiscrepancyIds() {
      long decisionTreeId = 1L;
      long featureVectorId = 2L;
      given(query.listArchivedDiscrepancyIds(
          new ReasoningBranchIdDto(decisionTreeId, featureVectorId))).willReturn(emptyList());

      get(discrepancyIdsUrlWith(decisionTreeId, featureVectorId))
          .contentType(anything())
          .statusCode(OK.value())
          .body("size()", is(0));
    }

    @TestWithRole(roles = { BUSINESS_OPERATOR })
    void its200WithCorrectBody_whenFound() {
      long decisionTreeId = 2L;
      long featureVectorId = 3L;
      given(query.listArchivedDiscrepancyIds(
          new ReasoningBranchIdDto(decisionTreeId, featureVectorId))).willReturn(List.of(22L, 33L));

      get(discrepancyIdsUrlWith(decisionTreeId, featureVectorId))
          .contentType(anything())
          .statusCode(OK.value())
          .body("size()", is(2))
          .body("[0]", is(22))
          .body("[1]", is(33));
    }

    @TestWithRole(roles = { BUSINESS_OPERATOR })
    void its400_whenBranchIdIncorrect() {
      get(discrepancyIdsUrlWith("abc"))
          .contentType(anything())
          .statusCode(BAD_REQUEST.value());

      get(discrepancyIdsUrlWith("abc-bcd"))
          .contentType(anything())
          .statusCode(BAD_REQUEST.value());
    }

    private String discrepancyIdsUrlWith(
        long decisionTreeId, long featureVectorId) {
      return format(
          DISCREPANCY_IDS_URL_TEMPLATE,
          format("%d-%d", decisionTreeId, featureVectorId), DiscrepancyStatus.ARCHIVED);
    }

    private String discrepancyIdsUrlWith(String branchId) {
      return format(DISCREPANCY_IDS_URL_TEMPLATE, branchId, DiscrepancyStatus.ARCHIVED);
    }
  }

  @Nested
  class DiscrepancyIdsWithInvalidUrl {

    @TestWithRole(roles = { ANALYST, AUDITOR })
    void its403_whenNotPermittedRole() {
      get(format(DISCREPANCY_IDS_URL_TEMPLATE, "1-1", ACTIVE)).statusCode(FORBIDDEN.value());
    }

    @TestWithRole(roles = { BUSINESS_OPERATOR })
    void its400_whenRequiredParamIsMissing() {
      get(format(DISCREPANCY_IDS_WITHOUT_REQUIRED_PARAM_URL_TEMPLATE, "1-1"));
    }

    @TestWithRole(roles = { BUSINESS_OPERATOR })
    void its400_whenRequiredParamIsMalformed() {
      get(format(DISCREPANCY_IDS_URL_TEMPLATE, "1-1", "undefined"));
    }
  }
}

