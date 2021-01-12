package com.silenteight.serp.governance.policy.step;

import com.silenteight.proto.governance.v1.api.FeatureVectorSolution;
import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.serp.governance.common.web.exception.GenericExceptionControllerAdvice;
import com.silenteight.serp.governance.policy.domain.StepType;
import com.silenteight.serp.governance.policy.domain.dto.StepDto;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.util.UUID;

import static com.silenteight.proto.governance.v1.api.FeatureVectorSolution.SOLUTION_FALSE_POSITIVE;
import static com.silenteight.proto.governance.v1.api.FeatureVectorSolution.SOLUTION_POTENTIAL_TRUE_POSITIVE;
import static com.silenteight.sens.governance.common.testing.rest.TestRoles.*;
import static com.silenteight.serp.governance.policy.domain.StepType.BUSINESS_LOGIC;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Import({ PolicyStepsRequestRestController.class, GenericExceptionControllerAdvice.class })
class PolicyStepsRequestRestControllerTest extends BaseRestControllerTest {

  private static final UUID POLICY_ID = UUID.randomUUID();
  private static final String POLICY_STEPS_ORDER_URL = "/v1/policies/" + POLICY_ID + "/steps";
  private static final String FIRST_STEP_NAME = "FIRST_STEP";
  private static final StepType FIRST_STEP_TYPE = BUSINESS_LOGIC;
  private static final String FIRST_STEP_DESCRIPTION = "FIRST_STEP_DESCRIPTION";
  private static final FeatureVectorSolution FIRST_SOLUTION = SOLUTION_FALSE_POSITIVE;
  private static final String SECOND_STEP_NAME = "SECOND_STEP";
  private static final StepType SECOND_STEP_TYPE = BUSINESS_LOGIC;
  private static final String SECOND_STEP_DESCRIPTION = "SECOND_STEP_DESCRIPTION";
  private static final FeatureVectorSolution SECOND_SOLUTION = SOLUTION_POTENTIAL_TRUE_POSITIVE;
  private static final UUID FIRST_STEP_ID = UUID.randomUUID();
  private static final StepDto FIRST_STEP = StepDto
      .builder()
      .id(FIRST_STEP_ID)
      .name(FIRST_STEP_NAME)
      .type(FIRST_STEP_TYPE)
      .description(FIRST_STEP_DESCRIPTION)
      .solution(FIRST_SOLUTION)
      .build();
  private static final UUID SECOND_STEP_ID = UUID.randomUUID();
  private static final StepDto SECOND_STEP = StepDto
      .builder()
      .id(SECOND_STEP_ID)
      .name(SECOND_STEP_NAME)
      .type(SECOND_STEP_TYPE)
      .description(SECOND_STEP_DESCRIPTION)
      .solution(SECOND_SOLUTION)
      .build();

  @MockBean
  private PolicyStepsRequestQuery policyStepsRequestQuery;

  @TestWithRole(roles = { POLICY_MANAGER })
  void its200_whenNoLogic() {
    given(policyStepsRequestQuery.listSteps(POLICY_ID)).willReturn(emptyList());

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
    given(policyStepsRequestQuery.listSteps(POLICY_ID)).willReturn(asList(FIRST_STEP, SECOND_STEP));

    get(POLICY_STEPS_ORDER_URL)
        .statusCode(OK.value())
        .body("[0].id", is(FIRST_STEP_ID.toString()))
        .body("[0].name", is(FIRST_STEP_NAME))
        .body("[0].type", is(FIRST_STEP_TYPE.toString()))
        .body("[0].description", is(FIRST_STEP_DESCRIPTION))
        .body("[0].solution", is(FIRST_SOLUTION.toString()))
        .body("[1].id", is(SECOND_STEP_ID.toString()))
        .body("[1].name", is(SECOND_STEP_NAME))
        .body("[0].type", is(SECOND_STEP_TYPE.toString()))
        .body("[1].description", is(SECOND_STEP_DESCRIPTION))
        .body("[1].solution", is(SECOND_SOLUTION.toString()));
  }
}
