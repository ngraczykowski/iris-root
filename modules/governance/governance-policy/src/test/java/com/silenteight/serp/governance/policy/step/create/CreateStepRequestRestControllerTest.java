package com.silenteight.serp.governance.policy.step.create;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.serp.governance.common.web.exception.GenericExceptionControllerAdvice;
import com.silenteight.serp.governance.policy.domain.PolicyService;
import com.silenteight.serp.governance.policy.domain.dto.CreateStepRequest;
import com.silenteight.serp.governance.policy.domain.dto.Solution;
import com.silenteight.serp.governance.policy.step.create.dto.CreateStepDto;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.UUID;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.*;
import static com.silenteight.serp.governance.policy.domain.StepType.NARROW;
import static com.silenteight.serp.governance.policy.domain.TestFixtures.STEP_NAME;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.CoreMatchers.anything;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@Import({
    CreateStepRequestRestController.class,
    CreateStepConfiguration.class,
    GenericExceptionControllerAdvice.class })
class CreateStepRequestRestControllerTest extends BaseRestControllerTest {

  private static final UUID POLICY_ID = UUID.randomUUID();
  private static final String CREATE_STEP_URL = "/v1/policies/" + POLICY_ID + "/steps";

  private static final UUID STEP_ID = UUID.randomUUID();
  private static final String DESCRIPTION = "description";
  private static final Solution SOLUTION = Solution.FALSE_POSITIVE;

  @MockBean
  private PolicyService policyService;

  @ParameterizedTest
  @MethodSource("com.silenteight.serp.governance.policy.domain.TestFixtures#getStepNames")
  @WithMockUser(username = USERNAME, authorities = MODEL_TUNER)
  void its202_whenStepAdded(String stepName) {
    post(CREATE_STEP_URL, new CreateStepDto(STEP_ID, stepName, DESCRIPTION, SOLUTION, NARROW))
        .contentType(anything())
        .statusCode(NO_CONTENT.value());

    ArgumentCaptor<CreateStepRequest> captor = ArgumentCaptor.forClass(CreateStepRequest.class);
    verify(policyService).addStepToPolicy(captor.capture());
    assertThat(captor.getValue().getPolicyId()).isEqualTo(POLICY_ID);
    assertThat(captor.getValue().getSolution()).isEqualTo(SOLUTION.getFeatureVectorSolution());
    assertThat(captor.getValue().getStepId()).isEqualTo(STEP_ID);
    assertThat(captor.getValue().getStepName()).isEqualTo(stepName);
    assertThat(captor.getValue().getStepDescription()).isEqualTo(DESCRIPTION);
    assertThat(captor.getValue().getStepType()).isEqualTo(NARROW);
    assertThat(captor.getValue().getCreatedBy()).isEqualTo(USERNAME);
  }

  @TestWithRole(roles = { APPROVER, USER_ADMINISTRATOR, QA, AUDITOR })
  void its403_whenNotPermittedRole() {
    post(CREATE_STEP_URL, new CreateStepDto(STEP_ID, STEP_NAME, DESCRIPTION, SOLUTION, NARROW))
        .contentType(anything())
        .statusCode(FORBIDDEN.value());
  }

  @ParameterizedTest
  @MethodSource("com.silenteight.serp.governance.policy.domain.TestFixtures#getIncorrectStepNames")
  @WithMockUser(username = USERNAME, authorities = MODEL_TUNER)
  void its400_whenStepNameLengthIsWrong(String stepName) {
    post(CREATE_STEP_URL, new CreateStepDto(STEP_ID, stepName, DESCRIPTION, SOLUTION, NARROW))
        .contentType(anything())
        .statusCode(BAD_REQUEST.value());
  }
}
