package com.silenteight.serp.governance.policy.step.edit;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.serp.governance.common.web.exception.GenericExceptionControllerAdvice;
import com.silenteight.serp.governance.policy.domain.PolicyService;
import com.silenteight.serp.governance.policy.domain.dto.Solution;
import com.silenteight.serp.governance.policy.domain.dto.UpdateStepRequest;
import com.silenteight.serp.governance.policy.step.edit.dto.EditStepDto;
import com.silenteight.serp.governance.policy.step.list.PolicyStepsRequestQuery;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.UUID;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.*;
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

  @ParameterizedTest
  @MethodSource("com.silenteight.serp.governance.policy.domain.TestFixtures#getStepNames")
  @WithMockUser(username = USERNAME, authorities = MODEL_TUNER)
  void its202_whenStepUpdated(String stepName) {
    when(policyStepsRequestQuery.getPolicyIdForStep(STEP_ID)).thenReturn(POLICY_ID);
    patch(STEP_URL, new EditStepDto(stepName, null, SOLUTION))
        .contentType(anything())
        .statusCode(NO_CONTENT.value());

    ArgumentCaptor<UpdateStepRequest> captor = ArgumentCaptor.forClass(UpdateStepRequest.class);
    verify(policyService).updateStep(captor.capture());
    assertThat(captor.getValue().getPolicyId()).isEqualTo(POLICY_ID);
    assertThat(captor.getValue().getStepId()).isEqualTo(STEP_ID);
    assertThat(captor.getValue().getName()).isEqualTo(stepName);
    assertThat(captor.getValue().getDescription()).isNull();
    assertThat(captor.getValue().getSolution()).isEqualTo(SOLUTION.getFeatureVectorSolution());
    assertThat(captor.getValue().getUpdatedBy()).isEqualTo(USERNAME);
  }

  @TestWithRole(roles = { APPROVER, USER_ADMINISTRATOR, QA, AUDITOR })
  void its403_whenNotPermittedRole() {
    patch(STEP_URL, new EditStepDto(NAME, null, SOLUTION))
        .contentType(anything())
        .statusCode(FORBIDDEN.value());
  }

  @ParameterizedTest
  @MethodSource("com.silenteight.serp.governance.policy.domain.TestFixtures#getIncorrectStepNames")
  @WithMockUser(username = USERNAME, authorities = MODEL_TUNER)
  void its400_whenStepNameLengthIsWrong(String stepName) {
    patch(STEP_URL, new EditStepDto(stepName, null, SOLUTION))
        .contentType(anything())
        .statusCode(BAD_REQUEST.value());
  }
}
