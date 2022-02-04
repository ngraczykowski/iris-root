package com.silenteight.serp.governance.policy.step.logic.edit;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.serp.governance.common.web.exception.GenericExceptionControllerAdvice;
import com.silenteight.serp.governance.policy.domain.PolicyService;
import com.silenteight.serp.governance.policy.domain.dto.ConfigurePolicyRequest.FeatureConfiguration;
import com.silenteight.serp.governance.policy.domain.dto.ConfigurePolicyRequest.FeatureLogicConfiguration;
import com.silenteight.serp.governance.policy.domain.dto.ConfigureStepLogicRequest;
import com.silenteight.serp.governance.policy.step.list.PolicyStepsRequestQuery;
import com.silenteight.serp.governance.policy.step.logic.edit.dto.EditStepLogicDto;
import com.silenteight.serp.governance.policy.step.logic.edit.dto.FeatureLogicDto;
import com.silenteight.serp.governance.policy.step.logic.edit.dto.MatchConditionDto;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Collection;
import java.util.UUID;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.*;
import static com.silenteight.serp.governance.policy.domain.Condition.IS;
import static java.util.Collections.emptyList;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.CoreMatchers.anything;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Import({ EditStepLogicRequestRestController.class,
          EditStepLogicConfiguration.class,
          GenericExceptionControllerAdvice.class })
class EditStepLogicRequestRestControllerTest extends BaseRestControllerTest {

  private static final UUID STEP_ID = UUID.randomUUID();
  private static final String EDIT_LOGIC_URL = "/v1/steps/" + STEP_ID + "/logic";
  private static final String FEATURE_VALUE = "MATCH";
  private static final Long POLICY_ID = 1L;
  private static final int TO_FULFILL = 1;

  @MockBean
  private PolicyService policyService;

  @MockBean
  private PolicyStepsRequestQuery policyStepsRequestQuery;

  @ParameterizedTest
  @MethodSource("com.silenteight.serp.governance.policy.domain.TestFixtures#getFeatureNames")
  @WithMockUser(username = USERNAME, authorities = MODEL_TUNER)
  void its202_whenLogicSaved(String featureName) {
    when(policyStepsRequestQuery.getPolicyIdForStep(STEP_ID)).thenReturn(POLICY_ID);

    put(EDIT_LOGIC_URL, new EditStepLogicDto(getFeatureLogicDto(featureName)))
        .contentType(anything())
        .statusCode(ACCEPTED.value());

    ArgumentCaptor<ConfigureStepLogicRequest> captor = ArgumentCaptor
        .forClass(ConfigureStepLogicRequest.class);
    verify(policyService).configureStepLogic(captor.capture());
    assertThat(captor.getValue().getPolicyId()).isEqualTo(POLICY_ID);
    assertThat(captor.getValue().getStepId()).isEqualTo(STEP_ID);
    assertThat(captor.getValue().getFeatureLogicConfigurations())
        .isEqualTo(getFeatureLogicConfiguration(featureName));
    assertThat(captor.getValue().getEditedBy()).isEqualTo(USERNAME);
  }

  private Collection<FeatureLogicDto> getFeatureLogicDto(String featureName) {
    return of(
        FeatureLogicDto
            .builder()
            .toFulfill(TO_FULFILL)
            .features(getMatchConditionDto(featureName))
            .build()
    );
  }

  private Collection<MatchConditionDto> getMatchConditionDto(String featureName) {
    MatchConditionDto matchConditionDto = MatchConditionDto
        .builder()
        .name(featureName)
        .condition(IS)
        .values(of(FEATURE_VALUE))
        .build();

    return of(matchConditionDto);
  }

  private Collection<FeatureLogicConfiguration> getFeatureLogicConfiguration(
      String featureName) {
    FeatureLogicConfiguration featureLogicConfiguration = FeatureLogicConfiguration
        .builder()
        .toFulfill(TO_FULFILL)
        .featureConfigurations(getFeatureConfiguration(featureName))
        .build();

    return of(featureLogicConfiguration);
  }

  private Collection<FeatureConfiguration> getFeatureConfiguration(String featureName) {
    FeatureConfiguration featureConfiguration = FeatureConfiguration
        .builder()
        .name(featureName)
        .condition(IS)
        .values(of(FEATURE_VALUE))
        .build();

    return of(featureConfiguration);
  }

  @TestWithRole(roles = { APPROVER, USER_ADMINISTRATOR, QA, AUDITOR })
  void its403_whenNotPermittedRole() {
    put(EDIT_LOGIC_URL, new EditStepLogicDto(emptyList()))
        .contentType(anything())
        .statusCode(FORBIDDEN.value());
  }

  @ParameterizedTest
  @MethodSource(
      "com.silenteight.serp.governance.policy.domain.TestFixtures#getIncorrectFeatureNames"
  )
  @WithMockUser(username = USERNAME, authorities = MODEL_TUNER)
  void its400_whenFeatureNameLengthIsWrong(String featureName) {
    put(EDIT_LOGIC_URL, new EditStepLogicDto(getFeatureLogicDto(featureName)))
        .contentType(anything())
        .statusCode(BAD_REQUEST.value());
  }
}
