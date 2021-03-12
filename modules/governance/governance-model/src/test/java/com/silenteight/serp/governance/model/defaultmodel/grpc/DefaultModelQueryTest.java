package com.silenteight.serp.governance.model.defaultmodel.grpc;

import com.silenteight.model.api.v1.SolvingModel;
import com.silenteight.serp.governance.policy.current.CurrentPolicyProvider;
import com.silenteight.serp.governance.strategy.CurrentStrategyProvider;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.serp.governance.model.defaultmodel.grpc.DefaultModelQuery.DEFAULT_MODEL_NAME;
import static com.silenteight.serp.governance.policy.current.CurrentPolicyFixture.CURRENT_POLICY_NAME;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultModelQueryTest {

  private static final String CURRENT_STRATEGY_NAME = "strategies/USE_ANALYST_SOLUTION";

  @Mock
  private CurrentPolicyProvider currentPolicyProvider;

  @Mock
  private CurrentStrategyProvider currentStrategyProvider;

  private DefaultModelQuery underTest;

  @BeforeEach
  void init() {
    underTest = new DefaultModelQuery(currentStrategyProvider, currentPolicyProvider);
    setCorrectConfiguration();
  }

  @Test
  void shouldReturnDefaultModel() throws ModelMisconfiguredException {
    SolvingModel solvingModel = underTest.get();

    assertThat(solvingModel.getName()).isEqualTo(DEFAULT_MODEL_NAME);
    assertThat(solvingModel.getPolicyName()).isEqualTo(CURRENT_POLICY_NAME);
    assertThat(solvingModel.getStrategyName()).isEqualTo(CURRENT_STRATEGY_NAME);
  }

  @Test
  void shouldThrowIfPolicyIsNotSet() {
    when(currentPolicyProvider.getCurrentPolicy()).thenReturn(empty());

    assertThatThrownBy(() -> underTest.get())
        .isInstanceOf(ModelMisconfiguredException.class)
        .hasMessageContaining("policyName");
  }

  @Test
  void shouldThrowIfStrategyIsNotSet() {
    when(currentStrategyProvider.getCurrentStrategy()).thenReturn(empty());

    assertThatThrownBy(() -> underTest.get())
        .isInstanceOf(ModelMisconfiguredException.class)
        .hasMessageContaining("strategyName");
  }

  private void setCorrectConfiguration() {
    lenient().when(currentPolicyProvider.getCurrentPolicy())
        .thenReturn(of(CURRENT_POLICY_NAME));
    lenient().when(currentStrategyProvider.getCurrentStrategy())
        .thenReturn(of(CURRENT_STRATEGY_NAME));
  }
}
