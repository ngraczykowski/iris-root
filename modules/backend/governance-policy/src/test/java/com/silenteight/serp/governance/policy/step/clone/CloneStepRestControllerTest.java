package com.silenteight.serp.governance.policy.step.clone;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.serp.governance.common.web.exception.GenericExceptionControllerAdvice;
import com.silenteight.serp.governance.policy.domain.PolicyService;
import com.silenteight.serp.governance.policy.domain.dto.CloneStepRequest;
import com.silenteight.serp.governance.policy.domain.exception.StepNotFoundException;
import com.silenteight.serp.governance.policy.domain.exception.WrongBasePolicyException;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.*;
import static com.silenteight.serp.governance.policy.domain.TestFixtures.FIRST_STEP_ID;
import static com.silenteight.serp.governance.policy.domain.TestFixtures.OTHER_STEP_ID;
import static com.silenteight.serp.governance.policy.domain.TestFixtures.POLICY_ID;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Import({
    CloneStepConfiguration.class,
    CloneStepControllerAdvice.class,
    CloneStepRestController.class,
    GenericExceptionControllerAdvice.class, })
class CloneStepRestControllerTest extends BaseRestControllerTest {

  @MockBean
  private PolicyService policyService;

  private static final String CLONE_STEP_URL =
      "/v1/policies/"
          + POLICY_ID
          + "/steps/"
          + FIRST_STEP_ID
          + "/clone/steps/"
          + OTHER_STEP_ID;

  @TestWithRole(roles = { APPROVER, AUDITOR, QA, USER_ADMINISTRATOR })
  void its403_whenNotPermittedRole() {
    post(CLONE_STEP_URL)
        .contentType(anything())
        .statusCode(FORBIDDEN.value());
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = MODEL_TUNER)
  void its202_whenCloneStepUseCaseUsed() {

    post(CLONE_STEP_URL)
        .contentType(anything())
        .statusCode(ACCEPTED.value());

    ArgumentCaptor<CloneStepRequest> captor = ArgumentCaptor.forClass(CloneStepRequest.class);
    verify(policyService).cloneStep(captor.capture());
    assertThat(captor.getValue().getPolicyId()).isEqualTo(POLICY_ID);
    assertThat(captor.getValue().getBaseStepId()).isEqualTo(FIRST_STEP_ID);
    assertThat(captor.getValue().getNewStepId()).isEqualTo(OTHER_STEP_ID);
    assertThat(captor.getValue().getCreatedBy()).isEqualTo(USERNAME);
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = MODEL_TUNER)
  void its404_whenBaseStepDoesNotExist() {
    StepNotFoundException exception = new StepNotFoundException(FIRST_STEP_ID);
    when(policyService.cloneStep(any())).thenThrow(exception);
    post(CLONE_STEP_URL)
        .contentType(anything())
        .statusCode(NOT_FOUND.value())
        .body(containsString(exception.getMessage()));
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = MODEL_TUNER)
  void its404WhenPolicyDoesNotExist() {
    WrongBasePolicyException exception = new WrongBasePolicyException(POLICY_ID);
    when(policyService.cloneStep(any())).thenThrow(exception);
    post(CLONE_STEP_URL)
        .contentType(anything())
        .statusCode(NOT_FOUND.value())
        .body(containsString(exception.getMessage()));
  }
}
