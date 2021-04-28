package com.silenteight.serp.governance.model.provide.grpc;

import com.silenteight.model.api.v1.Feature;
import com.silenteight.model.api.v1.SolvingModel;
import com.silenteight.serp.governance.model.category.CategoryRegistry;
import com.silenteight.serp.governance.model.domain.ModelQuery;
import com.silenteight.serp.governance.model.domain.exception.ModelMisconfiguredException;
import com.silenteight.serp.governance.model.featureset.CurrentFeatureSetProvider;
import com.silenteight.serp.governance.strategy.CurrentStrategyProvider;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.silenteight.serp.governance.model.agent.config.AgentConfigFixture.NAME_AGENT_CONFIG_NAME;
import static com.silenteight.serp.governance.model.agent.details.AgentDetailsFixture.AGENT_FEATURE_NAME;
import static com.silenteight.serp.governance.model.agentconfigset.FeatureSetFixture.FEATURE_CONFIG_SET;
import static com.silenteight.serp.governance.model.category.CategoryFixture.APTYPE_CATEGORY;
import static com.silenteight.serp.governance.model.category.CategoryFixture.APTYPE_CATEGORY_NAME;
import static com.silenteight.serp.governance.model.fixture.ModelFixtures.*;
import static com.silenteight.serp.governance.policy.current.CurrentPolicyFixture.CURRENT_POLICY_NAME;
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
  private CurrentFeatureSetProvider currentFeatureSetProvider;

  @Mock
  private CategoryRegistry categoryRegistry;

  @Mock
  private ModelQuery modelQuery;

  private SolvingModelProvider underTest;

  @BeforeEach
  void init() throws ModelMisconfiguredException {
    underTest = new SolvingModelConfiguration().solvingModelProvider(
        currentStrategyProvider,
        currentFeatureSetProvider,
        categoryRegistry);
    setCorrectConfiguration();
  }

  @Test
  void shouldReturnDefaultModel() throws ModelMisconfiguredException {
    SolvingModel solvingModel = underTest.get(modelQuery.getDefault());

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
    SolvingModel solvingModel = underTest.get(modelQuery.get(MODEL_RESOURCE_NAME));

    assertThat(solvingModel.getName()).isEqualTo(MODEL_RESOURCE_NAME);
    assertThat(solvingModel.getPolicyName()).isEqualTo(POLICY);
    assertThat(solvingModel.getStrategyName()).isEqualTo(CURRENT_STRATEGY_NAME);
    assertThat(solvingModel.getFeaturesList()).containsExactlyInAnyOrder(
        Feature.newBuilder()
            .setName(AGENT_FEATURE_NAME)
            .setAgentConfig(NAME_AGENT_CONFIG_NAME)
            .build());
    assertThat(solvingModel.getCategoriesList()).containsExactlyInAnyOrder(APTYPE_CATEGORY_NAME);
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
    lenient().when(currentFeatureSetProvider.getCurrentFeatureSet())
        .thenReturn(FEATURE_CONFIG_SET);
    lenient().when(categoryRegistry.getAllCategories())
        .thenReturn(List.of(APTYPE_CATEGORY));
    lenient().when(modelQuery.get(MODEL_RESOURCE_NAME))
        .thenReturn(MODEL_DTO);
    lenient().when(modelQuery.getDefault())
        .thenReturn(DEFAULT_MODEL_DTO);
  }
}
