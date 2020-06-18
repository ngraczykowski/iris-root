package com.silenteight.sens.webapp.backend.changerequest.reject;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.audit.correlation.RequestCorrelation;
import com.silenteight.sens.webapp.backend.config.exception.GenericExceptionControllerAdvice;
import com.silenteight.sens.webapp.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.webapp.common.testing.rest.testwithrole.TestWithRole;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Map;
import java.util.UUID;

import static com.silenteight.sens.webapp.backend.changerequest.RestControllerTestUtils.defaultHeaders;
import static com.silenteight.sens.webapp.common.rest.RestConstants.CORRELATION_ID_HEADER;
import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.*;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Slf4j
@Import({ RejectChangeRequestRestController.class, GenericExceptionControllerAdvice.class })
class RejectChangeRequestRestControllerTest extends BaseRestControllerTest {

  @MockBean
  private RejectChangeRequestUseCase rejectChangeRequestUseCase;

  @Test
  @WithMockUser(username = USERNAME, roles = APPROVER)
  void its202_whenApproverCallsEndpoint() {
    long changeRequestId = 2L;

    patch(mappingForRejection(changeRequestId), defaultHeaders()).statusCode(ACCEPTED.value());
  }

  @Test
  @WithMockUser(username = USERNAME, roles = APPROVER)
  void callsApproveUseCase() {
    long changeRequestId = 2L;

    patch(mappingForRejection(changeRequestId), defaultHeaders());

    ArgumentCaptor<RejectChangeRequestCommand> commandCaptor =
        ArgumentCaptor.forClass(RejectChangeRequestCommand.class);
    verify(rejectChangeRequestUseCase).apply(commandCaptor.capture());

    RejectChangeRequestCommand command = commandCaptor.getValue();
    assertThat(command.getChangeRequestId()).isEqualTo(changeRequestId);
    assertThat(command.getRejectorUsername()).isEqualTo(USERNAME);
  }

  @TestWithRole(roles = { BUSINESS_OPERATOR, ADMIN, ANALYST, AUDITOR })
  void its403_whenNotPermittedRole() {
    long changeRequestId = 2L;

    patch(mappingForRejection(changeRequestId), defaultHeaders()).statusCode(FORBIDDEN.value());
  }

  @Test
  @WithMockUser(username = USERNAME, roles = APPROVER)
  void setsCorrelationIdInThreadLocal() {
    long changeRequestId = 2L;
    UUID correlationId = randomUUID();

    patch(mappingForRejection(changeRequestId), Map.of(CORRELATION_ID_HEADER, correlationId));

    assertThat(RequestCorrelation.id()).isEqualTo(correlationId);
  }

  @Test
  @WithMockUser(username = USERNAME, roles = APPROVER)
  void its400_ifNoCorrelationIdProvidedInHeader() {
    long changeRequestId = 2L;

    patch(mappingForRejection(changeRequestId))
        .statusCode(BAD_REQUEST.value())
        .body("key", equalTo("Missing request header"))
        .body("extras.headerName", equalTo("CorrelationId"));
  }

  private String mappingForRejection(long id) {
    return "/change-request/" + id + "/reject";
  }
}
