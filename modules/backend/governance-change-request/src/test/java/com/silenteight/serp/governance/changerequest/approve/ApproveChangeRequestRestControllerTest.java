package com.silenteight.serp.governance.changerequest.approve;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.serp.governance.changerequest.approve.dto.ApproveChangeRequestDto;
import com.silenteight.serp.governance.common.web.exception.GenericExceptionControllerAdvice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
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
    post(mappingForApproval(CHANGE_REQUEST_ID), makeApproveChangeRequestDto(APPROVER_COMMENT))
        .statusCode(NO_CONTENT.value());
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = APPROVER)
  void callsApproveUseCase() {
    post(mappingForApproval(CHANGE_REQUEST_ID), makeApproveChangeRequestDto(APPROVER_COMMENT));

    ArgumentCaptor<ApproveChangeRequestCommand> commandCaptor =
        ArgumentCaptor.forClass(ApproveChangeRequestCommand.class);
    verify(approveChangeRequestUseCase).activate(commandCaptor.capture());

    ApproveChangeRequestCommand command = commandCaptor.getValue();
    assertThat(command.getId()).isEqualTo(CHANGE_REQUEST_ID);
    assertThat(command.getApproverUsername()).isEqualTo(USERNAME);
    assertThat(command.getApproverComment()).isEqualTo(APPROVER_COMMENT);
  }

  @TestWithRole(roles = { USER_ADMINISTRATOR, MODEL_TUNER, AUDITOR, QA, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRole() {
    post(mappingForApproval(CHANGE_REQUEST_ID), makeApproveChangeRequestDto(APPROVER_COMMENT))
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

  @ParameterizedTest
  @MethodSource(
      "com.silenteight.serp.governance.changerequest.fixture.ChangeRequestFixtures#"
          + "getForbiddenCharsAsInput")
  @WithMockUser(username = USERNAME, authorities = APPROVER)
  void its400_whenApproverCommentContainsForbiddenCharacters(String comment) {
    post(
        mappingForApproval(CHANGE_REQUEST_ID), new ApproveChangeRequestDto(comment))
        .statusCode(BAD_REQUEST.value());
  }

  @ParameterizedTest
  @MethodSource(
      "com.silenteight.serp.governance.changerequest.fixture.ChangeRequestFixtures#"
          + "getAllowedCharsAsInput")
  @WithMockUser(username = USERNAME, authorities = APPROVER)
  void its204_whenApproverCommentContainsAllowedCharacters(String comment) {
    post(
        mappingForApproval(CHANGE_REQUEST_ID), new ApproveChangeRequestDto(comment))
        .statusCode(NO_CONTENT.value());
  }

  private String mappingForApproval(UUID changeRequestId) {
    return "/v1/changeRequests/" + changeRequestId.toString() + ":approve";
  }

  private static ApproveChangeRequestDto makeApproveChangeRequestDto(String comment) {
    return new ApproveChangeRequestDto(comment);
  }
}
