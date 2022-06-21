package com.silenteight.serp.governance.model.provide;

import com.silenteight.model.api.v1.Feature;
import com.silenteight.model.api.v1.SolvingModel;
import com.silenteight.serp.governance.model.domain.exception.ModelMisconfiguredException;
import com.silenteight.serp.governance.model.get.ModelDetailsQuery;
import com.silenteight.serp.governance.policy.step.logic.PolicyStepsMatchConditionsNamesProvider;
import com.silenteight.serp.governance.strategy.CurrentStrategyProvider;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.silenteight.serp.governance.agent.domain.file.config.AgentConfigFixture.NAME_AGENT_CONFIG_NAME;
import static com.silenteight.serp.governance.agent.domain.file.details.AgentDetailsFixture.AGENT_FEATURE_NAME;
import static com.silenteight.serp.governance.model.category.CategoryFixture.APTYPE_CATEGORY_NAME;
import static com.silenteight.serp.governance.model.category.CategoryFixture.ISDENY_CATEGORY_NAME;
import static com.silenteight.serp.governance.model.fixture.ModelFixtures.*;
import static com.silenteight.serp.governance.policy.current.CurrentPolicyFixture.CURRENT_POLICY_NAME;
import static com.silenteight.serp.governance.policy.current.CurrentPolicyFixture.CURRENT_POLICY_UUID;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SolvingModelProviderTest {

  private static final String CURRENT_STRATEGY_NAME = "strategies/USE_ANALYST_SOLUTION";

  @Mock
  private CurrentStrategyProvider currentStrategyProvider;

  @Mock
  private PolicyFeatureProvider policyFeatureProvider;

  @Mock
  private PolicyStepsMatchConditionsNamesProvider matchConditionsNamesProvider;

  @Mock
  private DefaultModelQuery defaultModelQuery;

  @Mock
  private ModelDetailsQuery modelDetailsQuery;

  private SolvingModelProvider underTest;

  @BeforeEach
  void init() throws ModelMisconfiguredException {
    underTest = new ProvideConfiguration().solvingModelProvider(
        currentStrategyProvider,
        policyFeatureProvider,
        matchConditionsNamesProvider);
    setCorrectConfiguration();
  }

  @Test
  void shouldReturnDefaultModel() throws ModelMisconfiguredException {
    SolvingModel solvingModel = underTest.get(defaultModelQuery.getDefault());

    assertThat(solvingModel.getName()).isEqualTo(DEFAULT_MODEL_NAME);
    assertThat(solvingModel.getPolicyName()).isEqualTo(CURRENT_POLICY_NAME);
    assertThat(solvingModel.getStrategyName()).isEqualTo(CURRENT_STRATEGY_NAME);
    assertThat(solvingModel.getFeaturesList()).containsExactlyInAnyOrder(
        Feature.newBuilder()
            .setName(AGENT_FEATURE_NAME)
            .setAgentConfig(NAME_AGENT_CONFIG_NAME)
            .build());
    assertThat(solvingModel.getCategoriesList()).containsExactlyInAnyOrder(APTYPE_CATEGORY_NAME);
  }

  @Test
  void shouldReturnModel() throws ModelMisconfiguredException {
    SolvingModel solvingModel = underTest.get(MODEL_DTO);

    assertThat(solvingModel.getName()).isEqualTo(MODEL_RESOURCE_NAME);
    assertThat(solvingModel.getPolicyName()).isEqualTo(POLICY);
    assertThat(solvingModel.getStrategyName()).isEqualTo(CURRENT_STRATEGY_NAME);
    assertThat(solvingModel.getFeaturesList()).containsExactlyInAnyOrder(
        Feature.newBuilder()
            .setName(AGENT_FEATURE_NAME)
            .setAgentConfig(NAME_AGENT_CONFIG_NAME)
            .build());
    assertThat(solvingModel.getCategoriesList()).containsExactlyInAnyOrder(ISDENY_CATEGORY_NAME);
  }

  @Test
  void shouldThrowIfStrategyIsNotSet() {
    when(currentStrategyProvider.getCurrentStrategy()).thenReturn(empty());

    assertThatThrownBy(() -> underTest.get(DEFAULT_MODEL_DTO))
        .isInstanceOf(ModelMisconfiguredException.class)
        .hasMessageContaining("strategyName");
  }

  private void setCorrectConfiguration() throws ModelMisconfiguredException {
    lenient().when(currentStrategyProvider.getCurrentStrategy())
        .thenReturn(of(CURRENT_STRATEGY_NAME));
    lenient().when(modelDetailsQuery.getByPolicy(MODEL_RESOURCE_NAME))
        .thenReturn(List.of(MODEL_DTO));
    lenient().when(defaultModelQuery.getDefault())
        .thenReturn(DEFAULT_MODEL_DTO);
    lenient().when(matchConditionsNamesProvider.getMatchConditionsNames(CURRENT_POLICY_UUID))
        .thenReturn(List.of(AGENT_FEATURE_NAME, APTYPE_CATEGORY_NAME));
    lenient().when(matchConditionsNamesProvider.getMatchConditionsNames(POLICY_ID))
        .thenReturn(List.of(AGENT_FEATURE_NAME, ISDENY_CATEGORY_NAME));
    lenient().when(policyFeatureProvider.resolveFeatures(any())).thenReturn(List.of(getFeature()));
  }

  private Feature getFeature() {
    return Feature.newBuilder()
        .setName(AGENT_FEATURE_NAME)
        .setAgentConfig(NAME_AGENT_CONFIG_NAME)
        .build();
  }
}
