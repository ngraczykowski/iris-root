package com.silenteight.serp.governance.changerequest.cancel;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.serp.governance.changerequest.cancel.dto.CancelChangeRequestDto;
import com.silenteight.serp.governance.common.web.exception.GenericExceptionControllerAdvice;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.UUID;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.*;
import static com.silenteight.serp.governance.changerequest.fixture.ChangeRequestFixtures.CANCELLER_COMMENT;
import static com.silenteight.serp.governance.changerequest.fixture.ChangeRequestFixtures.CHANGE_REQUEST_ID;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Import({
    CancelChangeRequestRestController.class,
    GenericExceptionControllerAdvice.class
})
class CancelChangeRequestRestControllerTest extends BaseRestControllerTest {

  @MockBean
  private CancelChangeRequestUseCase cancelChangeRequestUseCase;

  private static final String USERNAME = "username";

  @Test
  @WithMockUser(username = USERNAME, authorities = BUSINESS_OPERATOR)
  void its202_whenBusinessOperatorCallsEndpoint() {
    patch(mappingForCancellation(CHANGE_REQUEST_ID), makeCancelChangeRequestDto())
        .statusCode(ACCEPTED.value());
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = BUSINESS_OPERATOR)
  void callsCancelUseCase() {
    patch(mappingForCancellation(CHANGE_REQUEST_ID), makeCancelChangeRequestDto());

    ArgumentCaptor<CancelChangeRequestCommand> commandCaptor =
        ArgumentCaptor.forClass(CancelChangeRequestCommand.class);
    verify(cancelChangeRequestUseCase).activate(commandCaptor.capture());

    CancelChangeRequestCommand command = commandCaptor.getValue();
    assertThat(command.getId()).isEqualTo(CHANGE_REQUEST_ID);
    assertThat(command.getCancellerUsername()).isEqualTo(USERNAME);
    assertThat(command.getCancellerComment()).isEqualTo(CANCELLER_COMMENT);
  }

  @TestWithRole(roles = { APPROVER, ADMINISTRATOR, ANALYST, AUDITOR, POLICY_MANAGER })
  void its403_whenNotPermittedRole() {

    patch(mappingForCancellation(CHANGE_REQUEST_ID), makeCancelChangeRequestDto())
        .statusCode(FORBIDDEN.value());
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = BUSINESS_OPERATOR)
  void its400_ifNoCancellerCommentInRequestBody() {
    patch(mappingForCancellation(CHANGE_REQUEST_ID), new CancelChangeRequestDto(null))
        .statusCode(BAD_REQUEST.value())
        .body("key", equalTo("MethodArgumentNotValid"))
        .body("extras.errors", hasItem("cancellerComment must not be blank"));
  }

  private String mappingForCancellation(UUID changeRequestId) {
    return "/changeRequests/" + changeRequestId.toString() + ":cancel";
  }

  private static CancelChangeRequestDto makeCancelChangeRequestDto() {
    return new CancelChangeRequestDto(CANCELLER_COMMENT);
  }
}
