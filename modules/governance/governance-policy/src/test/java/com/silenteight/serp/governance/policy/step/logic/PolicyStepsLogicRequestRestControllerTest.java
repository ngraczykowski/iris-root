package com.silenteight.serp.governance.policy.step.logic;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.serp.governance.common.web.exception.GenericExceptionControllerAdvice;
import com.silenteight.serp.governance.policy.domain.dto.FeatureLogicDto;
import com.silenteight.serp.governance.policy.domain.dto.FeaturesLogicDto;
import com.silenteight.serp.governance.policy.domain.dto.MatchConditionDto;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.util.Collection;
import java.util.UUID;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.*;
import static com.silenteight.serp.governance.policy.domain.Condition.IS;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Import({ PolicyStepsLogicRequestRestController.class, GenericExceptionControllerAdvice.class })
class PolicyStepsLogicRequestRestControllerTest extends BaseRestControllerTest {

  private static final UUID STEP_ID = UUID.randomUUID();
  private static final String POLICY_STEPS_LOGIC_URL = "/v1/steps/" + STEP_ID + "/logic";
  private static final FeaturesLogicDto LOGIC_DTO = getFeaturesLogicDto();
  private static final String AGENT_NAME = "AGENT";
  private static final String FIRST_VALUE = "FIRST_VALUE";
  private static final String SECOND_VALUE = "SECOND_VALUE";

  private static FeaturesLogicDto getFeaturesLogicDto() {
    return FeaturesLogicDto
        .builder()
        .featuresLogic(getLogicDto())
        .build();
  }

  private static Collection<FeatureLogicDto> getLogicDto() {
    MatchConditionDto featureDto = MatchConditionDto
        .builder()
        .name(AGENT_NAME)
        .condition(IS)
        .values(asList(FIRST_VALUE, SECOND_VALUE))
        .build();
    FeatureLogicDto featureLogicDto1 = FeatureLogicDto
        .builder()
        .toFulfill(2)
        .features(asList(featureDto, featureDto))
        .build();
    FeatureLogicDto featureLogicDto2 = FeatureLogicDto
        .builder()
        .toFulfill(1)
        .features(asList(featureDto))
        .build();
    return asList(featureLogicDto1, featureLogicDto2);
  }

  @MockBean
  private FeatureLogicRequestQuery policyStepsLogicRequestQuery;

  @TestWithRole(roles = { POLICY_MANAGER })
  void its200_whenNoLogic() {
    given(policyStepsLogicRequestQuery.listStepsFeaturesLogic(STEP_ID)).willReturn(
        FeaturesLogicDto.builder().build());

    get(POLICY_STEPS_LOGIC_URL)
        .contentType(anything())
        .statusCode(OK.value())
        .body("size()", is(1))
        .body("[0].featuresLogic", nullValue());
  }

  @TestWithRole(roles = { APPROVER, ADMINISTRATOR, ANALYST, AUDITOR, BUSINESS_OPERATOR })
  void its403_whenNotPermittedRole() {
    get(POLICY_STEPS_LOGIC_URL).statusCode(FORBIDDEN.value());
  }

  @TestWithRole(roles = { POLICY_MANAGER })
  void its200_whenPoliciesFound() {
    given(policyStepsLogicRequestQuery.listStepsFeaturesLogic(STEP_ID)).willReturn(LOGIC_DTO);

    get(POLICY_STEPS_LOGIC_URL)
        .statusCode(OK.value())
        .body("featuresLogic[0].toFulfill", is(2))
        .body("featuresLogic[0].features[0].name", is(AGENT_NAME))
        .body("featuresLogic[0].features[0].values[0]", is(FIRST_VALUE))
        .body("featuresLogic[0].features[0].values[1]", is(SECOND_VALUE))
        .body("featuresLogic[0].features[1].values[0]", is(FIRST_VALUE))
        .body("featuresLogic[0].features[1].values[1]", is(SECOND_VALUE))
        .body("featuresLogic[1].toFulfill", is(1))
        .body("featuresLogic[1].features[0].name", is(AGENT_NAME))
        .body("featuresLogic[1].features[0].values[0]", is(FIRST_VALUE))
        .body("featuresLogic[1].features[0].values[1]", is(SECOND_VALUE));
  }
}
