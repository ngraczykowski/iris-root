package com.silenteight.serp.governance.qa.manage.analysis.update;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.serp.governance.common.web.exception.GenericExceptionControllerAdvice;
import com.silenteight.serp.governance.qa.manage.analysis.details.DecisionAlreadyExistsException;
import com.silenteight.serp.governance.qa.manage.analysis.update.dto.UpdateAnalysisDecisionDto;
import com.silenteight.serp.governance.qa.manage.common.AlertControllerAdvice;
import com.silenteight.serp.governance.qa.manage.domain.dto.UpdateDecisionRequest;
import com.silenteight.serp.governance.qa.manage.domain.exception.WrongDiscriminatorException;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.*;
import static com.silenteight.serp.governance.qa.AlertFixture.DISCRIMINATOR;
import static com.silenteight.serp.governance.qa.manage.domain.DecisionLevel.ANALYSIS;
import static com.silenteight.serp.governance.qa.manage.domain.DecisionState.FAILED;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Import({
    UpdateAnalysisDecisionRestController.class,
    AlertControllerAdvice.class,
    UpdateAnalysisConfiguration.class,
    GenericExceptionControllerAdvice.class
})
class UpdateAnalysisDecisionRestControllerTest extends BaseRestControllerTest {

  private static final String UPDATE_DECISION_URL = format("/v1/qa/0/alerts/%s", DISCRIMINATOR);
  private static final String DECISION_COMMENT_FAILED = "FAILED";

  @MockBean
  UpdateAnalysisDecisionUseCase useCase;

  @Test
  @WithMockUser(username = USERNAME, authorities = QA)
  void its404_whenAlertNotFound() {
    WrongDiscriminatorException exception = new WrongDiscriminatorException(DISCRIMINATOR);
    doThrow(exception).when(useCase).activate(any());

    patch(UPDATE_DECISION_URL, getUpdateAnalysisDecisionDto()).statusCode(
        NOT_FOUND.value())
        .body(containsString(exception.getMessage()));
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = QA)
  void its400_whenDecisionAlreadyExists() {
    DecisionAlreadyExistsException exception = new DecisionAlreadyExistsException(
        DISCRIMINATOR,
        ANALYSIS);
    doThrow(exception).when(useCase).activate(any());

    patch(UPDATE_DECISION_URL, getUpdateAnalysisDecisionDto()).statusCode(BAD_REQUEST.value())
        .body(containsString(exception.getMessage()));
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = QA)
  void its200_whenUseCaseUsed() {
    //given
    ArgumentCaptor<UpdateDecisionRequest> commandCaptor =
        ArgumentCaptor.forClass(UpdateDecisionRequest.class);
    //when
    patch(UPDATE_DECISION_URL, getUpdateAnalysisDecisionDto())
        .statusCode(ACCEPTED.value());
    //then
    verify(useCase, times(1)).activate(commandCaptor.capture());
    UpdateDecisionRequest request = commandCaptor.getValue();
    assertThat(request.getDiscriminator()).isEqualTo(DISCRIMINATOR);
    assertThat(request.getState()).isEqualTo(FAILED);
    assertThat(request.getComment()).isEqualTo(DECISION_COMMENT_FAILED);
    assertThat(request.getLevel()).isEqualTo(ANALYSIS);
    assertThat(request.getCreatedBy()).isEqualTo(USERNAME);
    assertThat(request.getCreatedAt()).isNotNull();
  }

  @TestWithRole(roles = { APPROVER, AUDITOR, USER_ADMINISTRATOR, MODEL_TUNER, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRole() {
    patch(UPDATE_DECISION_URL, getUpdateAnalysisDecisionDto()).statusCode(FORBIDDEN.value());
  }

  private UpdateAnalysisDecisionDto getUpdateAnalysisDecisionDto() {
    return UpdateAnalysisDecisionDto
        .builder()
        .state(FAILED)
        .comment(DECISION_COMMENT_FAILED)
        .build();
  }
}
