package com.silenteight.sens.webapp.backend.changerequest.create;

import com.silenteight.sens.webapp.audit.correlation.RequestCorrelation;
import com.silenteight.sens.webapp.backend.config.exception.GenericExceptionControllerAdvice;
import com.silenteight.sens.webapp.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.webapp.common.testing.rest.testwithrole.TestWithRole;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

import static com.silenteight.sens.webapp.backend.changerequest.RestControllerTestUtils.defaultHeaders;
import static com.silenteight.sens.webapp.common.rest.RestConstants.CORRELATION_ID_HEADER;
import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.*;
import static java.time.OffsetDateTime.now;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Import({ CreateChangeRequestRestController.class, GenericExceptionControllerAdvice.class })
class CreateChangeRequestRestControllerTest extends BaseRestControllerTest {

  @MockBean
  private CreateChangeRequestUseCase createChangeRequestUseCase;

  @Test
  @WithMockUser(username = USERNAME, roles = BUSINESS_OPERATOR)
  void its202_whenBusinessOperatorCallsEndpoint() {
    post(mappingForChangeRequests(), changeRequestWithDefaults(), defaultHeaders())
        .statusCode(ACCEPTED.value());
  }

  @Test
  @WithMockUser(username = USERNAME, roles = BUSINESS_OPERATOR)
  void callsCreateUseCase() {
    UUID bulkChangeId = randomUUID();
    String comment = "comment ABC";
    OffsetDateTime createdAt = now();

    post(
        mappingForChangeRequests(),
        new CreateChangeRequestDto(bulkChangeId, createdAt, comment),
        defaultHeaders());

    ArgumentCaptor<CreateChangeRequestCommand> commandCaptor =
        ArgumentCaptor.forClass(CreateChangeRequestCommand.class);
    verify(createChangeRequestUseCase).apply(commandCaptor.capture());

    CreateChangeRequestCommand command = commandCaptor.getValue();
    assertThat(command.getBulkChangeId()).isEqualTo(bulkChangeId);
    assertThat(command.getMakerComment()).isEqualTo(comment);
    assertThat(command.getMakerUsername()).isEqualTo(USERNAME);
    assertThat(command.getCreatedAt()).isEqualTo(createdAt);
  }

  @Test
  @WithMockUser(username = USERNAME, roles = BUSINESS_OPERATOR)
  void setsCorrelationIdInThreadLocal() {
    UUID correlationId = randomUUID();
    post(
        mappingForChangeRequests(),
        changeRequestWithDefaults(),
        Map.of(CORRELATION_ID_HEADER, correlationId));

    assertThat(RequestCorrelation.id()).isEqualTo(correlationId);
  }

  @TestWithRole(roles = { APPROVER, ADMIN, ANALYST, AUDITOR })
  void its403_whenNotPermittedRole() {
    post(mappingForChangeRequests(), changeRequestWithDefaults(), defaultHeaders())
        .statusCode(FORBIDDEN.value());
  }

  @Test
  @WithMockUser(username = USERNAME, roles = BUSINESS_OPERATOR)
  void its400_ifNoCorrelationIdProvidedInHeader() {
    post(
        mappingForChangeRequests(),
        changeRequestWithDefaults())
        .statusCode(BAD_REQUEST.value())
        .body("key", equalTo("Missing request header"))
        .body("extras.headerName", equalTo("CorrelationId"));
  }

  private CreateChangeRequestDto changeRequestWithDefaults() {
    return new CreateChangeRequestDto(randomUUID(), now(), "Comment ABC");
  }

  private String mappingForChangeRequests() {
    return "/change-requests";
  }
}
