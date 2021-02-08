package com.silenteight.serp.governance.policy.step.edit;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.serp.governance.common.web.exception.GenericExceptionControllerAdvice;
import com.silenteight.serp.governance.policy.domain.PolicyService;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;
import java.util.UUID;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.*;
import static java.util.List.of;
import static org.hamcrest.CoreMatchers.anything;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Import({ SetPolicyStepsOrderRequestRestController.class,
          EditStepConfiguration.class,
          GenericExceptionControllerAdvice.class })
class SetPolicyStepsOrderRequestRestControllerTest extends BaseRestControllerTest {

  private static final UUID POLICY_ID = UUID.randomUUID();
  private static final UUID FIRST_STEP = UUID.randomUUID();
  private static final UUID SECOND_STEP = UUID.randomUUID();
  private static final UUID THIRD_STEP = UUID.randomUUID();

  private static final String EDIT_POLICY_URL = "/v1/policies/%s/steps-order";
  private static final String STEPS_ORDER_URL = String.format(EDIT_POLICY_URL, POLICY_ID);

  @MockBean
  private PolicyService policyService;

  @Test
  @WithMockUser(username = USERNAME, authorities = POLICY_MANAGER)
  void its200_whenPolicyEdited() {
    List<UUID> steps = of(FIRST_STEP, SECOND_STEP, THIRD_STEP);
    put(STEPS_ORDER_URL, steps)
        .contentType(anything())
        .statusCode(OK.value());

    verify(policyService).setStepsOrder(POLICY_ID, steps, USERNAME);
  }

  @TestWithRole(roles = { APPROVER, ADMINISTRATOR, ANALYST, AUDITOR, BUSINESS_OPERATOR })
  void its403_whenNotPermittedRole() {
    List<UUID> steps = of(FIRST_STEP, SECOND_STEP, THIRD_STEP);
    put(STEPS_ORDER_URL, steps)
        .contentType(anything())
        .statusCode(FORBIDDEN.value());
  }
}
