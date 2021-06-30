package com.silenteight.serp.governance.qa.validation.update;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.serp.governance.common.web.exception.GenericExceptionControllerAdvice;
import com.silenteight.serp.governance.qa.analysis.details.DecisionAlreadyExistsException;
import com.silenteight.serp.governance.qa.common.AlertControllerAdvice;
import com.silenteight.serp.governance.qa.domain.dto.UpdateDecisionRequest;
import com.silenteight.serp.governance.qa.domain.exception.WrongAlertNameException;
import com.silenteight.serp.governance.qa.validation.update.dto.UpdateValidationDecisionDto;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.UUID;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.*;
import static com.silenteight.serp.governance.qa.domain.DecisionLevel.VALIDATION;
import static com.silenteight.serp.governance.qa.domain.DecisionState.FAILED;
import static java.lang.String.format;
import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Import({
    UpdateValidationDecisionRestController.class,
    AlertControllerAdvice.class,
    UpdateValidationConfiguration.class,
    GenericExceptionControllerAdvice.class
})
class UpdateValidationDecisionRestControllerTest extends BaseRestControllerTest {

  private static final UUID ALERT_ID = fromString("015d8483-b3b1-467f-801a-3530037f220a");
  private static final String ALERT_NAME = format("alerts/%s", ALERT_ID);
  private static final String UPDATE_DECISION_URL = format("/v1/qa/1/alerts/%s", ALERT_ID);
  private static final String DECISION_COMMENT_FAILED = "FAILED";

  @MockBean
  UpdateValidationDecisionUseCase useCase;

  @Test
  @WithMockUser(username = USERNAME, authorities = QA_ISSUE_MANAGER)
  void its404_whenAlertNotFound() {
    WrongAlertNameException exception = new WrongAlertNameException(ALERT_NAME);

    doThrow(exception).when(useCase).activate(any());

    patch(UPDATE_DECISION_URL, updateValidationDecisionDto()).statusCode(
        NOT_FOUND.value())
        .body(containsString(exception.getMessage()));
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = QA_ISSUE_MANAGER)
  void its400_whenDecisionAlreadyExists() {
    DecisionAlreadyExistsException exception = new DecisionAlreadyExistsException(
        ALERT_NAME,
        VALIDATION);

    doThrow(exception).when(useCase).activate(any());

    patch(UPDATE_DECISION_URL, updateValidationDecisionDto()).statusCode(BAD_REQUEST.value())
        .body(containsString(exception.getMessage()));
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = QA_ISSUE_MANAGER)
  void its200_whenUseCaseUsed() {
    //given
    ArgumentCaptor<UpdateDecisionRequest> commandCaptor =
        ArgumentCaptor.forClass(UpdateDecisionRequest.class);
    //when
    patch(UPDATE_DECISION_URL, updateValidationDecisionDto())
        .statusCode(ACCEPTED.value());
    //then
    verify(useCase, times(1)).activate(commandCaptor.capture());
    UpdateDecisionRequest request = commandCaptor.getValue();
    assertThat(request.getAlertName()).isEqualTo(ALERT_NAME);
    assertThat(request.getState()).isEqualTo(FAILED);
    assertThat(request.getComment()).isEqualTo(DECISION_COMMENT_FAILED);
    assertThat(request.getLevel()).isEqualTo(VALIDATION);
    assertThat(request.getCreatedBy()).isEqualTo(USERNAME);
    assertThat(request.getCreatedAt()).isNotNull();
  }

  @TestWithRole(roles = { APPROVER, USER_ADMINISTRATOR, AUDITOR, QA, MODEL_TUNER })
  void its403_whenNotPermittedRole() {
    patch(UPDATE_DECISION_URL, updateValidationDecisionDto()).statusCode(FORBIDDEN.value());
  }

  private UpdateValidationDecisionDto updateValidationDecisionDto() {
    return UpdateValidationDecisionDto
        .builder()
        .decision(FAILED)
        .comment(DECISION_COMMENT_FAILED)
        .build();
  }
}
