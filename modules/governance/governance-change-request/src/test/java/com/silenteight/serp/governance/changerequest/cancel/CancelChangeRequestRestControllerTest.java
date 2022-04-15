package com.silenteight.serp.governance.changerequest.cancel;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.serp.governance.common.web.exception.GenericExceptionControllerAdvice;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.UUID;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.*;
import static com.silenteight.serp.governance.changerequest.fixture.ChangeRequestFixtures.CHANGE_REQUEST_ID;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@Import({
    CancelChangeRequestRestController.class,
    GenericExceptionControllerAdvice.class
})
class CancelChangeRequestRestControllerTest extends BaseRestControllerTest {

  @MockBean
  private CancelChangeRequestUseCase cancelChangeRequestUseCase;

  private static final String USERNAME = "username";

  @Test
  @WithMockUser(username = USERNAME, authorities = MODEL_TUNER)
  void its204_whenModelTunerCallsEndpoint() {
    post(mappingForCancellation(CHANGE_REQUEST_ID))
        .statusCode(NO_CONTENT.value());
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = MODEL_TUNER)
  void callsCancelUseCase() {
    post(mappingForCancellation(CHANGE_REQUEST_ID));

    ArgumentCaptor<CancelChangeRequestCommand> commandCaptor =
        ArgumentCaptor.forClass(CancelChangeRequestCommand.class);
    verify(cancelChangeRequestUseCase).activate(commandCaptor.capture());

    CancelChangeRequestCommand command = commandCaptor.getValue();
    assertThat(command.getId()).isEqualTo(CHANGE_REQUEST_ID);
    assertThat(command.getCancellerUsername()).isEqualTo(USERNAME);
  }

  @TestWithRole(roles = { USER_ADMINISTRATOR, APPROVER, AUDITOR, QA, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRole() {
    post(mappingForCancellation(CHANGE_REQUEST_ID))
        .statusCode(FORBIDDEN.value());
  }

  private String mappingForCancellation(UUID changeRequestId) {
    return "/v1/changeRequests/" + changeRequestId.toString() + ":cancel";
  }
}
