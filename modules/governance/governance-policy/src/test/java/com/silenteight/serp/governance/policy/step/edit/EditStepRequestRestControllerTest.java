package com.silenteight.serp.governance.policy.step.edit;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.serp.governance.common.web.exception.GenericExceptionControllerAdvice;
import com.silenteight.serp.governance.policy.domain.PolicyService;
import com.silenteight.serp.governance.policy.domain.dto.Solution;
import com.silenteight.serp.governance.policy.domain.dto.UpdateStepRequest;
import com.silenteight.serp.governance.policy.step.edit.dto.EditStepDto;
import com.silenteight.serp.governance.policy.step.list.PolicyStepsRequestQuery;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.UUID;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.*;
import static com.silenteight.serp.governance.policy.domain.TestFixtures.STEP_NAME_THAT_EXCEEDED_MAX_LENGTH;
import static com.silenteight.serp.governance.policy.domain.TestFixtures.STEP_NAME_WITH_MAX_LENGTH;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.CoreMatchers.anything;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@Import({
    EditStepRequestRestController.class,
    EditStepConfiguration.class,
    GenericExceptionControllerAdvice.class })
class EditStepRequestRestControllerTest extends BaseRestControllerTest {

  private static final UUID STEP_ID = UUID.randomUUID();
  private static final String STEP_URL = "/v1/steps/" + STEP_ID;

  private static final long POLICY_ID = 123L;

  private static final String NAME = "name";
  private static final Solution SOLUTION = Solution.FALSE_POSITIVE;

  @MockBean
  private PolicyService policyService;
  @MockBean
  private PolicyStepsRequestQuery policyStepsRequestQuery;

  @Test
  @WithMockUser(username = USERNAME, authorities = MODEL_TUNER)
  void its202_whenStepUpdated() {
    when(policyStepsRequestQuery.getPolicyIdForStep(STEP_ID)).thenReturn(POLICY_ID);
    patch(STEP_URL, new EditStepDto(NAME, null, SOLUTION))
        .contentType(anything())
        .statusCode(NO_CONTENT.value());

    ArgumentCaptor<UpdateStepRequest> captor = ArgumentCaptor.forClass(UpdateStepRequest.class);
    verify(policyService).updateStep(captor.capture());
    assertThat(captor.getValue().getPolicyId()).isEqualTo(POLICY_ID);
    assertThat(captor.getValue().getStepId()).isEqualTo(STEP_ID);
    assertThat(captor.getValue().getName()).isEqualTo(NAME);
    assertThat(captor.getValue().getDescription()).isNull();
    assertThat(captor.getValue().getSolution()).isEqualTo(SOLUTION.getFeatureVectorSolution());
    assertThat(captor.getValue().getUpdatedBy()).isEqualTo(USERNAME);
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = MODEL_TUNER)
  void its202_whenStepUpdatedWithMaxAllowedNameLength() {
    when(policyStepsRequestQuery.getPolicyIdForStep(STEP_ID)).thenReturn(POLICY_ID);
    patch(STEP_URL, new EditStepDto(STEP_NAME_WITH_MAX_LENGTH, null, SOLUTION))
        .contentType(anything())
        .statusCode(NO_CONTENT.value());

    ArgumentCaptor<UpdateStepRequest> captor = ArgumentCaptor.forClass(UpdateStepRequest.class);
    verify(policyService).updateStep(captor.capture());
    assertThat(captor.getValue().getName()).isEqualTo(STEP_NAME_WITH_MAX_LENGTH);
  }

  @TestWithRole(roles = { APPROVER, USER_ADMINISTRATOR, QA, AUDITOR })
  void its403_whenNotPermittedRole() {
    patch(STEP_URL, new EditStepDto(NAME, null, SOLUTION))
        .contentType(anything())
        .statusCode(FORBIDDEN.value());
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = MODEL_TUNER)
  void its400_whenStepNameLengthGreaterThanAllowed() {
    patch(STEP_URL, new EditStepDto(STEP_NAME_THAT_EXCEEDED_MAX_LENGTH, null, SOLUTION))
        .contentType(anything())
        .statusCode(BAD_REQUEST.value());
  }
}
