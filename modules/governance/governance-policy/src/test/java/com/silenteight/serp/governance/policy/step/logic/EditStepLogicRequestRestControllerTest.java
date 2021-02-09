package com.silenteight.serp.governance.policy.step.logic;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.serp.governance.common.web.exception.GenericExceptionControllerAdvice;
import com.silenteight.serp.governance.policy.domain.PolicyService;
import com.silenteight.serp.governance.policy.domain.dto.ConfigurePolicyRequest.FeatureConfiguration;
import com.silenteight.serp.governance.policy.domain.dto.ConfigurePolicyRequest.FeatureLogicConfiguration;
import com.silenteight.serp.governance.policy.step.list.PolicyStepsRequestQuery;
import com.silenteight.serp.governance.policy.step.logic.dto.EditStepLogicDto;
import com.silenteight.serp.governance.policy.step.logic.dto.FeatureLogicDto;
import com.silenteight.serp.governance.policy.step.logic.dto.MatchConditionDto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Collection;
import java.util.UUID;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.*;
import static com.silenteight.serp.governance.policy.domain.Condition.IS;
import static java.util.Collections.emptyList;
import static java.util.List.of;
import static org.hamcrest.CoreMatchers.anything;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Import({ EditStepLogicRequestRestController.class,
          EditStepLogicConfiguration.class,
          GenericExceptionControllerAdvice.class })
class EditStepLogicRequestRestControllerTest extends BaseRestControllerTest {

  private static final UUID STEP_ID = UUID.randomUUID();
  private static final String EDIT_LOGIC_URL = "/v1/steps/" + STEP_ID.toString() + "/logic";
  private static final String FEATURE_NAME = "name";
  private static final String FEATURE_VALUE = "MATCH";
  private static final Long POLICY_ID = 1L;
  private static final int TO_FULFILL = 1;

  @MockBean
  private PolicyService policyService;

  @MockBean
  private PolicyStepsRequestQuery policyStepsRequestQuery;

  @Test
  @WithMockUser(username = USERNAME, authorities = POLICY_MANAGER)
  void its202_whenLogicSaved() {
    when(policyStepsRequestQuery.getPolicyIdForStep(STEP_ID)).thenReturn(POLICY_ID);
    
    put(EDIT_LOGIC_URL, new EditStepLogicDto(getFeatureLogicDto()))
        .contentType(anything())
        .statusCode(ACCEPTED.value());

    verify(policyService).configureStepLogic(
        POLICY_ID, STEP_ID, getFeatureLogicConfiguration(), USERNAME);
  }

  private Collection<FeatureLogicDto> getFeatureLogicDto() {
    return of(
        FeatureLogicDto.builder().toFulfill(TO_FULFILL).features(getMatchConditionDto()).build()
    );
  }

  private Collection<MatchConditionDto> getMatchConditionDto() {
    MatchConditionDto matchConditionDto = MatchConditionDto
        .builder()
        .name(FEATURE_NAME)
        .condition(IS)
        .values(of(FEATURE_VALUE))
        .build();

    return of(matchConditionDto);
  }

  private Collection<FeatureLogicConfiguration> getFeatureLogicConfiguration() {
    FeatureLogicConfiguration featureLogicConfiguration = FeatureLogicConfiguration
        .builder()
        .toFulfill(TO_FULFILL)
        .featureConfigurations(getFeatureConfiguration())
        .build();

    return of(featureLogicConfiguration);
  }

  private Collection<FeatureConfiguration> getFeatureConfiguration() {
    FeatureConfiguration featureConfiguration = FeatureConfiguration
        .builder()
        .name(FEATURE_NAME)
        .condition(IS)
        .values(of(FEATURE_VALUE))
        .build();

    return of(featureConfiguration);
  }

  @TestWithRole(roles = { APPROVER, ADMINISTRATOR, ANALYST, AUDITOR, BUSINESS_OPERATOR })
  void its403_whenNotPermittedRole() {
    put(EDIT_LOGIC_URL, new EditStepLogicDto(emptyList()))
        .contentType(anything())
        .statusCode(FORBIDDEN.value());
  }

}
