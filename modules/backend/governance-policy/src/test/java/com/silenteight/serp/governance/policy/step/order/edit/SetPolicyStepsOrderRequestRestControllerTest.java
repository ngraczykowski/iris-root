package com.silenteight.serp.governance.policy.step.order.edit;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.serp.governance.common.web.exception.GenericExceptionControllerAdvice;
import com.silenteight.serp.governance.policy.domain.PolicyService;
import com.silenteight.serp.governance.policy.domain.dto.SetStepsOrderRequest;
import com.silenteight.serp.governance.policy.step.list.PolicyStepsRequestQuery;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;
import java.util.UUID;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.MODEL_TUNER;
import static com.silenteight.sens.governance.common.testing.rest.TestRoles.QA;
import static com.silenteight.sens.governance.common.testing.rest.TestRoles.USER_ADMINISTRATOR;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.CoreMatchers.anything;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Import({ SetPolicyStepsOrderRequestRestController.class,
          SetPolicyStepsOrderConfiguration.class,
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

  @MockBean
  private PolicyStepsRequestQuery policyStepsRequestQuery;

  @Test
  @WithMockUser(username = USERNAME, authorities = MODEL_TUNER)
  void its200_whenPolicyEdited() {
    List<UUID> steps = of(FIRST_STEP, SECOND_STEP, THIRD_STEP);
    put(STEPS_ORDER_URL, steps)
        .contentType(anything())
        .statusCode(OK.value());

    ArgumentCaptor<SetStepsOrderRequest> captor = ArgumentCaptor
        .forClass(SetStepsOrderRequest.class);
    verify(policyService).setStepsOrder(captor.capture());
    assertThat(captor.getValue().getPolicyId()).isEqualTo(POLICY_ID);
    assertThat(captor.getValue().getStepsOrder()).isEqualTo(steps);
    assertThat(captor.getValue().getUpdatedBy()).isEqualTo(USERNAME);
  }

  @TestWithRole(roles = { USER_ADMINISTRATOR, QA })
  void its403_whenNotPermittedRole() {
    List<UUID> steps = of(FIRST_STEP, SECOND_STEP, THIRD_STEP);
    put(STEPS_ORDER_URL, steps)
        .contentType(anything())
        .statusCode(FORBIDDEN.value());
  }
}
