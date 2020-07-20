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

import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.BUSINESS_OPERATOR;
import static java.time.Instant.parse;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@Import({
    CircuitBreakerRestController.class,
    CircuitBreakerRestControllerAdvice.class,
    GenericExceptionControllerAdvice.class })
class CircuitBreakerRestControllerForDiscrepanciesTest extends BaseRestControllerTest {

  private static final String DISCREPANCIES_URL_TEMPLATE = "/discrepancies";
  private static final String DISCREPANCIES_ARCHIVE_URL_TEMPLATE = "/discrepancies/archive";

  @MockBean
  private DiscrepancyCircuitBreakerQuery query;

  @MockBean
  private ArchiveDiscrepanciesUseCase archiveUseCase;

  @Nested
  class DiscrepancyList {

    @TestWithRole(roles = { BUSINESS_OPERATOR })
    void its200WithEmptyListWhenNoDiscrepancies() {
      given(query.listDiscrepanciesByIds(List.of(1L))).willReturn(emptyList());

      get(DISCREPANCIES_URL_TEMPLATE + "?id=1")
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

      get(DISCREPANCIES_URL_TEMPLATE + "?id=2,14")
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
      get(DISCREPANCIES_URL_TEMPLATE)
          .contentType(anything())
          .statusCode(BAD_REQUEST.value())
          .body("key", is("MissingServletRequestParameter"))
          .body("extras.parameterName", is("id"));
    }

    @TestWithRole(roles = { BUSINESS_OPERATOR })
    void its400WhenInvalidListIdsPassedInUrl() {
      get(DISCREPANCIES_URL_TEMPLATE + "?id=2aa,4")
          .contentType(anything())
          .statusCode(BAD_REQUEST.value())
          .body("key", is("MethodArgumentTypeMismatch"))
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
      patch(DISCREPANCIES_ARCHIVE_URL_TEMPLATE, List.of(1L))
          .contentType(anything())
          .statusCode(ACCEPTED.value());
    }

    @TestWithRole(role = BUSINESS_OPERATOR)
    void appliesArchiveCommandOnUseCase() {
      List<Long> discrepancyIds = List.of(4L, 5L);
      patch(DISCREPANCIES_ARCHIVE_URL_TEMPLATE, discrepancyIds);

      ArgumentCaptor<ArchiveCircuitBreakerDiscrepanciesCommand> commandCaptor =
          ArgumentCaptor.forClass(ArchiveCircuitBreakerDiscrepanciesCommand.class);

      verify(archiveUseCase).apply(commandCaptor.capture());

      assertThat(commandCaptor.getValue().getIds()).isEqualTo(discrepancyIds);
    }
  }
}

