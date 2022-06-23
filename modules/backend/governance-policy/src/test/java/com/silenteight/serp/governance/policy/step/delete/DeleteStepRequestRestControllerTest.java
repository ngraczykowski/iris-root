package com.silenteight.serp.governance.policy.step.delete;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.serp.governance.common.web.exception.GenericExceptionControllerAdvice;
import com.silenteight.serp.governance.policy.domain.PolicyService;
import com.silenteight.serp.governance.policy.domain.dto.DeleteStepRequest;
import com.silenteight.serp.governance.policy.step.list.PolicyStepsRequestQuery;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.UUID;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.*;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.CoreMatchers.anything;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@Import({
    DeleteStepRequestRestController.class,
    DeleteStepConfiguration.class,
    GenericExceptionControllerAdvice.class })
class DeleteStepRequestRestControllerTest extends BaseRestControllerTest {

  private static final UUID STEP_ID = UUID.randomUUID();
  private static final String DELETE_STEP_URL = "/v1/steps/" + STEP_ID;
  private static final long POLICY_ID = 123L;

  @MockBean
  private PolicyService policyService;
  @MockBean
  private PolicyStepsRequestQuery policyStepsRequestQuery;

  @Test
  @WithMockUser(username = USERNAME, authorities = MODEL_TUNER)
  void its202_whenStepDeleted() {
    when(policyStepsRequestQuery.getPolicyIdForStep(STEP_ID)).thenReturn(POLICY_ID);

    delete(DELETE_STEP_URL)
        .contentType(anything())
        .statusCode(NO_CONTENT.value());

    ArgumentCaptor<DeleteStepRequest> captor = ArgumentCaptor.forClass(DeleteStepRequest.class);
    verify(policyService).deleteStep(captor.capture());
    assertThat(captor.getValue().getStepId()).isEqualTo(STEP_ID);
    assertThat(captor.getValue().getPolicyId()).isEqualTo(POLICY_ID);
    assertThat(captor.getValue().getDeletedBy()).isEqualTo(USERNAME);
  }

  @TestWithRole(roles = { APPROVER, USER_ADMINISTRATOR, QA, AUDITOR })
  void its403_whenNotPermittedRole() {
    delete(DELETE_STEP_URL)
        .contentType(anything())
        .statusCode(FORBIDDEN.value());
  }
}
