package com.silenteight.sens.webapp.backend.bulkchange;

import com.silenteight.sens.webapp.audit.correlation.RequestCorrelation;
import com.silenteight.sens.webapp.backend.config.exception.GenericExceptionControllerAdvice;
import com.silenteight.sens.webapp.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.webapp.common.testing.rest.testwithrole.TestWithRole;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.silenteight.sens.webapp.common.rest.RestConstants.CORRELATION_ID_HEADER;
import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.*;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.time.OffsetDateTime.now;
import static java.util.Collections.emptyList;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Import({ BulkChangeRestController.class, GenericExceptionControllerAdvice.class })
class BulkChangeRestControllerTest extends BaseRestControllerTest {

  private static final String BULK_CHANGES_URL = "/bulk-changes";

  @MockBean
  private BulkChangeQuery bulkChangeQuery;

  @MockBean
  private CreateBulkChangeUseCase createBulkChangeUseCase;

  @TestWithRole(role = APPROVER)
  void its200WithEmptyListWhenNoPendingBulkChanges() {
    given(bulkChangeQuery.listPending()).willReturn(emptyList());

    get(BULK_CHANGES_URL)
        .contentType(anything())
        .statusCode(OK.value())
        .body("size()", is(0));
  }

  @TestWithRole(role = APPROVER)
  void its200WithCorrectBody_whenFound() {
    UUID id1 = randomUUID();
    UUID id2 = randomUUID();
    given(bulkChangeQuery.listPending()).willReturn(List.of(
        new BulkChangeDto(id1,
            List.of(new ReasoningBranchIdDto(1L, 12L), new ReasoningBranchIdDto(2L, 13L)),
            "FALSE_POSITIVE", TRUE, now()),
        new BulkChangeDto(
            id2, List.of(new ReasoningBranchIdDto(4L, 15L)), "POTENTIAL_TRUE_POSITIVE", FALSE,
            now()))
    );

    get(BULK_CHANGES_URL)
        .contentType(anything())
        .statusCode(OK.value())
        .body("[0].id", is(id1.toString()))
        .body("[0].reasoningBranchIds[0].decisionTreeId", is(1))
        .body("[0].reasoningBranchIds[0].featureVectorId", is(12))
        .body("[0].reasoningBranchIds[1].decisionTreeId", is(2))
        .body("[0].reasoningBranchIds[1].featureVectorId", is(13))
        .body("[0].aiSolution", is("FALSE_POSITIVE"))
        .body("[0].active", is(TRUE))

        .body("[1].id", is(id2.toString()))
        .body("[1].reasoningBranchIds[0].decisionTreeId", is(4))
        .body("[1].reasoningBranchIds[0].featureVectorId", is(15))
        .body("[1].aiSolution", is("POTENTIAL_TRUE_POSITIVE"))
        .body("[1].active", is(FALSE));
  }

  @TestWithRole(roles = { BUSINESS_OPERATOR, ADMIN, ANALYST, AUDITOR })
  void its403_whenNotPermittedRole() {
    get(BULK_CHANGES_URL).statusCode(FORBIDDEN.value());
  }

  @TestWithRole(role = BUSINESS_OPERATOR)
  void its200_onCreate() {
    post(BULK_CHANGES_URL, bulkChangeDtoWithDefaults(), defaultHeaders())
        .contentType(anything())
        .statusCode(OK.value());
  }

  @TestWithRole(role = BUSINESS_OPERATOR)
  void appliesCreateBulkChangeCommandOnUseCase() {
    UUID id = randomUUID();
    List<ReasoningBranchIdDto> reasoningBranchIds = List.of(new ReasoningBranchIdDto(1L, 2L));
    String solution = "FALSE_POSITIVE";
    Boolean active = TRUE;
    post(
        BULK_CHANGES_URL,
        new BulkChangeDto(id, reasoningBranchIds, solution, active, now()),
        defaultHeaders());

    ArgumentCaptor<CreateBulkChangeCommand> commandCaptor =
        ArgumentCaptor.forClass(CreateBulkChangeCommand.class);

    verify(createBulkChangeUseCase).apply(commandCaptor.capture());

    CreateBulkChangeCommand command = commandCaptor.getValue();
    assertThat(command.getBulkChangeId()).isEqualTo(id);
    assertThat(command.getAiSolution()).isEqualTo(solution);
    assertThat(command.getReasoningBranchIds()).isEqualTo(reasoningBranchIds);
    assertThat(command.getActive()).isEqualTo(active);
  }

  @Test
  @WithMockUser(roles = BUSINESS_OPERATOR)
  void setsCorrelationIdInThreadLocal() {
    UUID correlationId = randomUUID();
    post(
        BULK_CHANGES_URL,
        bulkChangeDtoWithDefaults(),
        Map.of(CORRELATION_ID_HEADER, correlationId));

    assertThat(RequestCorrelation.id()).isEqualTo(correlationId);
  }

  @Test
  @WithMockUser(roles = BUSINESS_OPERATOR)
  void its400_IfNoCorrelationIdProvidedInHeader() {
    post(BULK_CHANGES_URL, bulkChangeDtoWithDefaults())
        .statusCode(BAD_REQUEST.value())
        .body("key", equalTo("Missing request header"))
        .body("extras.headerName", equalTo("CorrelationId"));
  }

  @TestWithRole(role = APPROVER)
  void its403_onCreate_whenNotPermittedRole() {
    post(BULK_CHANGES_URL, bulkChangeDtoWithDefaults(), defaultHeaders())
        .contentType(anything())
        .statusCode(FORBIDDEN.value());
  }

  private BulkChangeDto bulkChangeDtoWithDefaults() {
    return new BulkChangeDto(
        randomUUID(), List.of(new ReasoningBranchIdDto(1L, 2L)), null, null, now());
  }

  private static Map<String, UUID> defaultHeaders() {
    return Map.of(CORRELATION_ID_HEADER, randomUUID());
  }
}
