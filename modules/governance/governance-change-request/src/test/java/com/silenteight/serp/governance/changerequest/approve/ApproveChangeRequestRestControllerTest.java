package com.silenteight.serp.governance.changerequest.approve;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.serp.governance.changerequest.approve.dto.ApproveChangeRequestDto;
import com.silenteight.serp.governance.common.web.exception.GenericExceptionControllerAdvice;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.UUID;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.*;
import static com.silenteight.serp.governance.changerequest.fixture.ChangeRequestFixtures.APPROVER_COMMENT;
import static com.silenteight.serp.governance.changerequest.fixture.ChangeRequestFixtures.CHANGE_REQUEST_ID;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@Import({
    ApproveChangeRequestRestController.class,
    GenericExceptionControllerAdvice.class
})
class ApproveChangeRequestRestControllerTest extends BaseRestControllerTest {

  private static final String USERNAME = "username";

  @MockBean
  private ApproveChangeRequestUseCase approveChangeRequestUseCase;

  @Test
  @WithMockUser(username = USERNAME, authorities = APPROVER)
  void its204_whenApproverCallsEndpoint() {
    post(mappingForApproval(CHANGE_REQUEST_ID), makeApproveChangeRequestDto())
        .statusCode(NO_CONTENT.value());
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = APPROVER)
  void callsApproveUseCase() {
    post(mappingForApproval(CHANGE_REQUEST_ID), makeApproveChangeRequestDto());

    ArgumentCaptor<ApproveChangeRequestCommand> commandCaptor =
        ArgumentCaptor.forClass(ApproveChangeRequestCommand.class);
    verify(approveChangeRequestUseCase).activate(commandCaptor.capture());

    ApproveChangeRequestCommand command = commandCaptor.getValue();
    assertThat(command.getId()).isEqualTo(CHANGE_REQUEST_ID);
    assertThat(command.getApproverUsername()).isEqualTo(USERNAME);
    assertThat(command.getApproverComment()).isEqualTo(APPROVER_COMMENT);
  }

  @TestWithRole(roles = { ADMINISTRATOR, ANALYST, AUDITOR, BUSINESS_OPERATOR, POLICY_MANAGER })
  void its403_whenNotPermittedRole() {
    post(mappingForApproval(CHANGE_REQUEST_ID), makeApproveChangeRequestDto())
        .statusCode(FORBIDDEN.value());
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = APPROVER)
  void its400_ifNoApproverCommentInRequestBody() {
    post(
        mappingForApproval(CHANGE_REQUEST_ID), new ApproveChangeRequestDto(null))
        .statusCode(BAD_REQUEST.value())
        .body("extras.errors", hasItem("approverComment must not be blank"));
  }

  private String mappingForApproval(UUID changeRequestId) {
    return "/v1/changeRequests/" + changeRequestId.toString() + ":approve";
  }

  private static ApproveChangeRequestDto makeApproveChangeRequestDto() {
    return new ApproveChangeRequestDto(APPROVER_COMMENT);
  }
}
