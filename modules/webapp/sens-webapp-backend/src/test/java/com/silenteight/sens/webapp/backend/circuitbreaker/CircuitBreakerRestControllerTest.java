package com.silenteight.sens.webapp.backend.circuitbreaker;

import com.silenteight.sens.webapp.backend.config.exception.GenericExceptionControllerAdvice;
import com.silenteight.sens.webapp.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.webapp.common.testing.rest.testwithrole.TestWithRole;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.util.List;

import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.ANALYST;
import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.AUDITOR;
import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.BUSINESS_OPERATOR;
import static java.lang.String.format;
import static java.time.Instant.now;
import static java.time.Instant.parse;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

@Import({
    CircuitBreakerRestController.class,
    GenericExceptionControllerAdvice.class,
    CircuitBreakerRestControllerAdvice.class })
class CircuitBreakerRestControllerTest extends BaseRestControllerTest {

  private static final String DISCREPANT_BRANCHES_URL = "/discrepant-branches";
  private static final String DISCREPANCY_IDS_URL = "/discrepant-branches/%s/discrepancy-ids";
  private static final String DISCREPANCIES_URL = "/discrepancies";
  private static final String DISCREPANCIES_ARCHIVE_URL = "/discrepancies/archive";

  @MockBean
  private DiscrepancyCircuitBreakerQuery query;

  @MockBean
  private ArchiveDiscrepanciesUseCase archiveUseCase;

  @Nested
  class DiscrepantBranchesList {

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
      long decisionTreeId1 = 1L;
      long featureVectorId1 = 12L;
      long decisionTreeId2 = 4L;
      long featureVectorId2 = 15L;
      given(query.listDiscrepantBranches()).willReturn(List.of(
          new DiscrepantBranchDto(
              new ReasoningBranchIdDto(decisionTreeId1, featureVectorId1), now()),
          new DiscrepantBranchDto(
              new ReasoningBranchIdDto(decisionTreeId2, featureVectorId2), now())));

      get(DISCREPANT_BRANCHES_URL)
          .contentType(anything())
          .statusCode(OK.value())
          .body("[0].branchId.decisionTreeId", is((int) decisionTreeId1))
          .body("[0].branchId.featureVectorId", is((int) featureVectorId1))
          .body("[1].branchId.decisionTreeId", is((int) decisionTreeId2))
          .body("[1].branchId.featureVectorId", is((int) featureVectorId2));
    }

    @TestWithRole(roles = { ANALYST, AUDITOR })
    void its403_whenNotPermittedRole() {
      get(DISCREPANT_BRANCHES_URL).statusCode(FORBIDDEN.value());
    }
  }

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
    void its404_whenBranchIdIncorrect() {
      get(discrepancyIdsUrlWith("abc"))
          .contentType(anything())
          .statusCode(NOT_FOUND.value());

      get(discrepancyIdsUrlWith("abc-bcd"))
          .contentType(anything())
          .statusCode(NOT_FOUND.value());
    }

    @TestWithRole(roles = { ANALYST, AUDITOR })
    void its403_whenNotPermittedRole() {
      get(discrepancyIdsUrlWith(1L, 1L)).statusCode(FORBIDDEN.value());
    }

    private String discrepancyIdsUrlWith(long decisionTreeId, long featureVectorId) {
      return format(DISCREPANCY_IDS_URL, format("%d-%d", decisionTreeId, featureVectorId));
    }

    private String discrepancyIdsUrlWith(String branchId) {
      return format(DISCREPANCY_IDS_URL, branchId);
    }
  }

  @Nested
  class DiscrepancyList {

    @TestWithRole(roles = { BUSINESS_OPERATOR })
    void its200WithEmptyListWhenNoDiscrepancies() {
      given(query.listDiscrepanciesByIds(List.of(1L))).willReturn(emptyList());

      get(DISCREPANCIES_URL + "?id=1")
          .contentType(anything())
          .statusCode(OK.value())
          .body("size()", is(0));
    }

    @TestWithRole(roles = { BUSINESS_OPERATOR })
    void its200WithCorrectBody_whenFound() {
      String aiCommentDate1 = "2020-05-29T10:16:11Z";
      String aiCommentDate2 = "2020-06-01T10:16:11Z";
      String analystCommentDate1 = "2020-05-30T11:16:11Z";
      String analystCommentDate2 = "2020-06-02T11:26:11Z";
      given(query.listDiscrepanciesByIds(List.of(2L, 14L))).willReturn(List.of(
          DiscrepancyDto.builder()
              .id(2L)
              .alertId("abc-123")
              .aiComment("ai comment ABC")
              .aiCommentDate(parse(aiCommentDate1))
              .analystComment("analyst comment ABC")
              .analystCommentDate(parse(analystCommentDate1))
              .build(),
          DiscrepancyDto.builder()
              .id(14L)
              .alertId("bcd-456")
              .aiComment("ai comment BCD")
              .aiCommentDate(parse(aiCommentDate2))
              .analystComment("analyst comment BCD")
              .analystCommentDate(parse(analystCommentDate2))
              .build()
      ));

      get(DISCREPANCIES_URL + "?id=2,14")
          .contentType(anything())
          .statusCode(OK.value())
          .body("size()", is(2))
          .body("[0].id", is(2))
          .body("[0].alertId", is("abc-123"))
          .body("[0].aiComment", is("ai comment ABC"))
          //TODO: investigate why date is incorrectly serialized in controller tests
          //.body("[0].aiCommentDate", is(aiCommentDate1))
          .body("[0].analystComment", is("analyst comment ABC"))
          //.body("[0].analystCommentDate", is(analystCommentDate1))

          .body("[1].id", is(14))
          .body("[1].alertId", is("bcd-456"))
          .body("[1].aiComment", is("ai comment BCD"))
          //.body("[1].aiCommentDate", is(aiCommentDate2))
          //.body("[1].analystCommentDate", is(analystCommentDate2))
          .body("[1].analystComment", is("analyst comment BCD"));
    }

    @TestWithRole(roles = { BUSINESS_OPERATOR })
    void its400WhenNoIdParam() {
      get(DISCREPANCIES_URL)
          .contentType(anything())
          .statusCode(BAD_REQUEST.value())
          .body("key", is("Missing request parameter"))
          .body("extras.parameterName", is("id"));
    }

    @TestWithRole(roles = { BUSINESS_OPERATOR })
    void its400WhenInvalidListIdsPassedInUrl() {
      get(DISCREPANCIES_URL + "?id=2aa,4")
          .contentType(anything())
          .statusCode(BAD_REQUEST.value())
          .body("key", is("Parameter type mismatch"))
          .body("extras.parameterName", is("id"));
    }
  }

  @Nested
  class ArchiveDiscrepancies {

    @BeforeEach
    void setUp() {
      //needed because of https://github.com/spring-projects/spring-boot/issues/12470
      reset(archiveUseCase);
    }

    @TestWithRole(role = BUSINESS_OPERATOR)
    void its202_onArchive() {
      patch(DISCREPANCIES_ARCHIVE_URL, List.of(1L))
          .contentType(anything())
          .statusCode(ACCEPTED.value());
    }

    @TestWithRole(role = BUSINESS_OPERATOR)
    void appliesArchiveCommandOnUseCase() {
      List<Long> discrepancyIds = List.of(4L, 5L);
      patch(DISCREPANCIES_ARCHIVE_URL, discrepancyIds);

      ArgumentCaptor<ArchiveCircuitBreakerDiscrepanciesCommand> commandCaptor =
          ArgumentCaptor.forClass(ArchiveCircuitBreakerDiscrepanciesCommand.class);

      verify(archiveUseCase).apply(commandCaptor.capture());

      assertThat(commandCaptor.getValue().getIds()).isEqualTo(discrepancyIds);
    }
  }
}

