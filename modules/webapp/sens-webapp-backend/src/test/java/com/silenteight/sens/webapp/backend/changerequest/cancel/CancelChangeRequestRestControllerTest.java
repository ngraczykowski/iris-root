package com.silenteight.sens.webapp.backend.changerequest.cancel;

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
import static org.hamcrest.CoreMatchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Import({ CancelChangeRequestRestController.class, GenericExceptionControllerAdvice.class })
class CancelChangeRequestRestControllerTest extends BaseRestControllerTest {

  @MockBean
  private CancelChangeRequestUseCase cancelChangeRequestUseCase;

  private static final String USERNAME = "username";

  @Test
  @WithMockUser(username = USERNAME, roles = BUSINESS_OPERATOR)
  void its202_whenBusinessOperatorCallsEndpoint() {
    long changeRequestId = 2L;

    patch(mappingForCancellation(changeRequestId), defaultBody(), defaultHeaders())
        .statusCode(ACCEPTED.value());
  }

  @Test
  @WithMockUser(username = USERNAME, roles = BUSINESS_OPERATOR)
  void callsCancelUseCase() {
    long changeRequestId = 2L;

    String cancellerComment = "comment ABCD";
    patch(
        mappingForCancellation(changeRequestId), new CancelChangeRequestDto(cancellerComment),
        defaultHeaders());

    ArgumentCaptor<CancelChangeRequestCommand> commandCaptor =
        ArgumentCaptor.forClass(CancelChangeRequestCommand.class);
    verify(cancelChangeRequestUseCase).apply(commandCaptor.capture());

    CancelChangeRequestCommand command = commandCaptor.getValue();
    assertThat(command.getChangeRequestId()).isEqualTo(changeRequestId);
    assertThat(command.getCancellerUsername()).isEqualTo(USERNAME);
    assertThat(command.getCancellerComment()).isEqualTo(cancellerComment);
  }

  @TestWithRole(roles = { APPROVER, ADMIN, ANALYST, AUDITOR })
  void its403_whenNotPermittedRole() {
    long changeRequestId = 2L;

    patch(
        mappingForCancellation(changeRequestId), defaultBody(), defaultHeaders())
        .statusCode(FORBIDDEN.value());
  }

  @Test
  @WithMockUser(username = USERNAME, roles = BUSINESS_OPERATOR)
  void setsCorrelationIdInThreadLocal() {
    long changeRequestId = 2L;
    UUID correlationId = randomUUID();

    patch(
        mappingForCancellation(changeRequestId), defaultBody(),
        Map.of(CORRELATION_ID_HEADER, correlationId));

    assertThat(RequestCorrelation.id()).isEqualTo(correlationId);
  }

  @Test
  @WithMockUser(username = USERNAME, roles = BUSINESS_OPERATOR)
  void its400_ifNoCorrelationIdProvidedInHeader() {
    long changeRequestId = 2L;

    patch(mappingForCancellation(changeRequestId), defaultBody())
        .statusCode(BAD_REQUEST.value())
        .body("key", equalTo("Missing request header"))
        .body("extras.headerName", equalTo("CorrelationId"));
  }

  @Test
  @WithMockUser(username = USERNAME, roles = APPROVER)
  void its400_ifNoApproverCommentInRequestBody() {
    patch(
        mappingForCancellation(1L), new CancelChangeRequestDto(null), defaultHeaders())
        .statusCode(BAD_REQUEST.value())
        .body("extras.errors", hasItem("cancellerComment must not be blank"));
  }

  private String mappingForCancellation(long id) {
    return "/change-request/" + id + "/cancel";
  }

  private static CancelChangeRequestDto defaultBody() {
    return new CancelChangeRequestDto("not_used");
  }
}
