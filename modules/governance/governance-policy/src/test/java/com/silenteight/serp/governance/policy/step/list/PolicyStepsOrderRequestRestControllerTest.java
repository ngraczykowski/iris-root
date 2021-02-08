package com.silenteight.serp.governance.policy.step.list;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.serp.governance.common.web.exception.GenericExceptionControllerAdvice;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.UUID;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.*;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Import({ PolicyStepsOrderRequestRestController.class, GenericExceptionControllerAdvice.class })
class PolicyStepsOrderRequestRestControllerTest extends BaseRestControllerTest {

  private static final UUID POLICY_ID = UUID.randomUUID();
  private static final String POLICY_STEPS_ORDER_URL = "/v1/policies/" + POLICY_ID + "/steps-order";
  private static final List<UUID> STEPS_ORDER = asList(
      UUID.randomUUID(),
      UUID.randomUUID(),
      UUID.randomUUID(),
      UUID.randomUUID());

  @MockBean
  private PolicyStepsOrderRequestQuery policyStepsOrderRequestQuery;

  @TestWithRole(roles = { POLICY_MANAGER })
  void its200_whenNoLogic() {
    given(policyStepsOrderRequestQuery.listStepsOrder(POLICY_ID)).willReturn(emptyList());

    get(POLICY_STEPS_ORDER_URL)
        .contentType(anything())
        .statusCode(OK.value())
        .body("size()", is(0));
  }

  @TestWithRole(roles = { APPROVER, ADMINISTRATOR, ANALYST, AUDITOR, BUSINESS_OPERATOR })
  void its403_whenNotPermittedRole() {
    get(POLICY_STEPS_ORDER_URL).statusCode(FORBIDDEN.value());
  }

  @TestWithRole(roles = { POLICY_MANAGER })
  void its200_whenPoliciesFound() {
    given(policyStepsOrderRequestQuery.listStepsOrder(POLICY_ID)).willReturn(STEPS_ORDER);

    get(POLICY_STEPS_ORDER_URL)
        .statusCode(OK.value())
        .body("size()", is(4))
        .body("[0]", is(STEPS_ORDER.get(0).toString()))
        .body("[1]", is(STEPS_ORDER.get(1).toString()))
        .body("[2]", is(STEPS_ORDER.get(2).toString()))
        .body("[3]", is(STEPS_ORDER.get(3).toString()));
  }
}
