package com.silenteight.sens.webapp.backend.changerequest.approve;

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

@Import({ ApproveChangeRequestRestController.class, GenericExceptionControllerAdvice.class })
class ApproveChangeRequestRestControllerTest extends BaseRestControllerTest {

  private static final String USERNAME = "username";

  @MockBean
  private ApproveChangeRequestUseCase approveChangeRequestUseCase;

  @Test
  @WithMockUser(username = USERNAME, roles = APPROVER)
  void its202_whenApproverCallsEndpoint() {
    long changeRequestId = 2L;

    patch(mappingForApproval(changeRequestId), defaultBody(), defaultHeaders())
        .statusCode(ACCEPTED.value());
  }

  @Test
  @WithMockUser(username = USERNAME, roles = APPROVER)
  void callsApproveUseCase() {
    long changeRequestId = 2L;
    String approverComment = "some comment";

    ApproveChangeRequestDto body = new ApproveChangeRequestDto(approverComment);
    patch(mappingForApproval(changeRequestId), body, defaultHeaders());

    ArgumentCaptor<ApproveChangeRequestCommand> commandCaptor =
        ArgumentCaptor.forClass(ApproveChangeRequestCommand.class);
    verify(approveChangeRequestUseCase).apply(commandCaptor.capture());

    ApproveChangeRequestCommand command = commandCaptor.getValue();
    assertThat(command.getApproverComment()).isEqualTo(approverComment);
    assertThat(command.getChangeRequestId()).isEqualTo(changeRequestId);
    assertThat(command.getApproverUsername()).isEqualTo(USERNAME);
  }

  @TestWithRole(roles = { BUSINESS_OPERATOR, ADMIN, ANALYST, AUDITOR })
  void its403_whenNotPermittedRole() {
    long changeRequestId = 2L;

    patch(mappingForApproval(changeRequestId), defaultBody(), defaultHeaders())
        .statusCode(FORBIDDEN.value());
  }

  @Test
  @WithMockUser(username = USERNAME, roles = APPROVER)
  void setsCorrelationIdInThreadLocal() {
    long changeRequestId = 2L;
    UUID correlationId = randomUUID();

    patch(
        mappingForApproval(changeRequestId), defaultBody(),
        Map.of(CORRELATION_ID_HEADER, correlationId));

    assertThat(RequestCorrelation.id()).isEqualTo(correlationId);
  }

  @Test
  @WithMockUser(username = USERNAME, roles = APPROVER)
  void its400_ifNoCorrelationIdProvidedInHeader() {
    long changeRequestId = 2L;

    patch(mappingForApproval(changeRequestId), defaultBody())
        .statusCode(BAD_REQUEST.value())
        .body("key", equalTo("Missing request header"))
        .body("extras.headerName", equalTo("CorrelationId"));
  }

  @Test
  @WithMockUser(username = USERNAME, roles = APPROVER)
  void its400_ifNoApproverCommentInRequestBody() {
    long changeRequestId = 2L;

    patch(
        mappingForApproval(changeRequestId), new ApproveChangeRequestDto(null),
        defaultHeaders())
        .statusCode(BAD_REQUEST.value())
        .body("extras.errors", hasItem("approverComment must not be blank"));
  }

  private static ApproveChangeRequestDto defaultBody() {
    return new ApproveChangeRequestDto("not_used");
  }

  private String mappingForApproval(long id) {
    return "/change-requests/" + id + "/approve";
  }
}
