package com.silenteight.sens.webapp.backend.bulkchange;

import com.silenteight.sens.webapp.audit.api.correlation.RequestCorrelation;
import com.silenteight.sens.webapp.backend.config.exception.GenericExceptionControllerAdvice;
import com.silenteight.sens.webapp.backend.reasoningbranch.list.dto.ReasoningBranchIdDto;
import com.silenteight.sens.webapp.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.webapp.common.testing.rest.testwithrole.TestWithRole;

import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.silenteight.sens.webapp.common.rest.RestConstants.CORRELATION_ID_HEADER;
import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.APPROVER;
import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.BUSINESS_OPERATOR;
import static java.lang.Boolean.TRUE;
import static java.time.OffsetDateTime.now;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Import({
    BulkChangeRestController.class,
    BulkChangeRestControllerAdvice.class,
    GenericExceptionControllerAdvice.class })
class BulkChangeRestControllerTest extends BaseRestControllerTest {

  private static final String CREATE_BULK_CHANGE_URL = "/bulk-changes";

  @MockBean
  private CreateBulkChangeUseCase createBulkChangeUseCase;

  @TestWithRole(role = BUSINESS_OPERATOR)
  void its202_onCreate() {
    post(CREATE_BULK_CHANGE_URL, bulkChangeDtoWithDefaults(), defaultHeaders())
        .contentType(anything())
        .statusCode(ACCEPTED.value());
  }

  @TestWithRole(role = BUSINESS_OPERATOR)
  void appliesCreateBulkChangeCommandOnUseCase() {
    reset(createBulkChangeUseCase);
    UUID id = randomUUID();
    List<ReasoningBranchIdDto> reasoningBranchIds = List.of(new ReasoningBranchIdDto(1L, 2L));
    String solution = "FALSE_POSITIVE";
    Boolean active = TRUE;
    post(
        CREATE_BULK_CHANGE_URL,
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

  @TestWithRole(role = BUSINESS_OPERATOR)
  void setsCorrelationIdInThreadLocal() {
    UUID correlationId = randomUUID();
    post(
        CREATE_BULK_CHANGE_URL,
        bulkChangeDtoWithDefaults(),
        Map.of(CORRELATION_ID_HEADER, correlationId));

    assertThat(RequestCorrelation.id()).isEqualTo(correlationId);
  }

  @TestWithRole(role = BUSINESS_OPERATOR)
  void its400_IfNoCorrelationIdProvidedInHeader() {
    post(CREATE_BULK_CHANGE_URL, bulkChangeDtoWithDefaults())
        .statusCode(BAD_REQUEST.value())
        .body("key", equalTo("MissingRequestHeader"))
        .body("extras.headerName", equalTo("CorrelationId"));
  }

  @TestWithRole(role = APPROVER)
  void its403_onCreate_whenNotPermittedRole() {
    post(CREATE_BULK_CHANGE_URL, bulkChangeDtoWithDefaults(), defaultHeaders())
        .contentType(anything())
        .statusCode(FORBIDDEN.value());
  }

  private BulkChangeDto bulkChangeDtoWithDefaults() {
    return new BulkChangeDto(
        randomUUID(), List.of(new ReasoningBranchIdDto(1L, 2L)), null, null, now());
  }

  private Map<String, UUID> defaultHeaders() {
    return Map.of(CORRELATION_ID_HEADER, randomUUID());
  }
}
