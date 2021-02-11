package com.silenteight.serp.governance.policy.step.edit;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.serp.governance.common.web.exception.GenericExceptionControllerAdvice;
import com.silenteight.serp.governance.policy.domain.PolicyService;
import com.silenteight.serp.governance.policy.domain.dto.Solution;
import com.silenteight.serp.governance.policy.step.edit.dto.EditStepDto;
import com.silenteight.serp.governance.policy.step.list.PolicyStepsRequestQuery;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.UUID;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.*;
import static org.hamcrest.CoreMatchers.anything;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@Import({ EditStepRequestRestController.class,
          EditStepConfiguration.class,
          GenericExceptionControllerAdvice.class })
class EditStepRequestRestControllerTest extends BaseRestControllerTest {

  private static final UUID STEP_ID = UUID.randomUUID();
  private static final String CREATE_STEP_URL = "/v1/steps/" + STEP_ID.toString();

  private static final long POLICY_ID = 123L;

  private static final String NAME = "name";
  private static final Solution SOLUTION = Solution.FALSE_POSITIVE;

  @MockBean
  private PolicyService policyService;
  @MockBean
  private PolicyStepsRequestQuery policyStepsRequestQuery;

  @Test
  @WithMockUser(username = USERNAME, authorities = POLICY_MANAGER)
  void its202_whenStepUpdated() {
    when(policyStepsRequestQuery.getPolicyIdForStep(STEP_ID)).thenReturn(POLICY_ID);

    patch(CREATE_STEP_URL, new EditStepDto(NAME, null, SOLUTION))
        .contentType(anything())
        .statusCode(NO_CONTENT.value());

    verify(policyService).updateStep(
        POLICY_ID,
        STEP_ID,
        NAME,
        null,
        SOLUTION.getFeatureVectorSolution(),
        USERNAME);
  }

  @TestWithRole(roles = { APPROVER, ADMINISTRATOR, ANALYST, AUDITOR, BUSINESS_OPERATOR })
  void its403_whenNotPermittedRole() {
    patch(CREATE_STEP_URL, new EditStepDto(NAME, null, SOLUTION))
        .contentType(anything())
        .statusCode(FORBIDDEN.value());
  }
}
