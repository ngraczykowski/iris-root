package com.silenteight.serp.governance.changerequest.reject;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.serp.governance.changerequest.reject.dto.RejectChangeRequestDto;
import com.silenteight.serp.governance.common.web.exception.GenericExceptionControllerAdvice;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.UUID;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.*;
import static com.silenteight.serp.governance.changerequest.fixture.ChangeRequestFixtures.CHANGE_REQUEST_ID;
import static com.silenteight.serp.governance.changerequest.fixture.ChangeRequestFixtures.REJECTOR_COMMENT;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@Import({
    RejectChangeRequestRestController.class,
    GenericExceptionControllerAdvice.class
})
class RejectChangeRequestRestControllerTest extends BaseRestControllerTest {

  @MockBean
  private RejectChangeRequestUseCase rejectChangeRequestUseCase;

  @Test
  @WithMockUser(username = USERNAME, authorities = APPROVER)
  void its204_whenApproverCallsEndpoint() {
    post(mappingForRejection(CHANGE_REQUEST_ID), makeRejectChangeRequestDto())
        .statusCode(NO_CONTENT.value());
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = APPROVER)
  void callsRejectUseCase() {
    post(mappingForRejection(CHANGE_REQUEST_ID), makeRejectChangeRequestDto());

    ArgumentCaptor<RejectChangeRequestCommand> commandCaptor =
        ArgumentCaptor.forClass(RejectChangeRequestCommand.class);
    verify(rejectChangeRequestUseCase).activate(commandCaptor.capture());

    RejectChangeRequestCommand command = commandCaptor.getValue();
    assertThat(command.getId()).isEqualTo(CHANGE_REQUEST_ID);
    assertThat(command.getRejectorUsername()).isEqualTo(USERNAME);
    assertThat(command.getRejectorComment()).isEqualTo(REJECTOR_COMMENT);
  }

  @TestWithRole(roles = { USER_ADMINISTRATOR, MODEL_TUNER, AUDITOR, QA, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRole() {
    post(mappingForRejection(CHANGE_REQUEST_ID), makeRejectChangeRequestDto()).statusCode(
        FORBIDDEN.value());
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = APPROVER)
  void its400_ifNoRejectorCommentInRequestBody() {
    post(
        mappingForRejection(CHANGE_REQUEST_ID), new RejectChangeRequestDto(null))
        .statusCode(BAD_REQUEST.value())
        .body("key", equalTo("MethodArgumentNotValid"))
        .body("extras.errors", hasItem("rejectorComment must not be blank"));
  }

  private String mappingForRejection(UUID changeRequestId) {
    return "/v1/changeRequests/" + changeRequestId.toString() + ":reject";
  }

  private static RejectChangeRequestDto makeRejectChangeRequestDto() {
    return new RejectChangeRequestDto(REJECTOR_COMMENT);
  }
}
