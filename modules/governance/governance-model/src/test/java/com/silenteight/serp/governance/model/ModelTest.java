package com.silenteight.serp.governance.model;

import com.silenteight.model.api.v1.SolvingModel;
import com.silenteight.serp.governance.model.defaultmodel.grpc.DefaultModelQuery;
import com.silenteight.serp.governance.policy.current.CurrentPolicyProvider;
import com.silenteight.serp.governance.strategy.CurrentStrategyProvider;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.silenteight.serp.governance.policy.current.CurrentPolicyFixture.CURRENT_POLICY_NAME;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = { ModelTestConfiguration.class })
@ExtendWith({ SpringExtension.class })
@AutoConfigureJsonTesters
class ModelTest {

  private static final String CURRENT_STRATEGY_NAME = "strategies/USE_ANALYST_SOLUTION";

  @Autowired
  DefaultModelQuery defaultModelQuery;

  @Autowired
  CurrentStrategyProvider currentStrategyProviderMock;

  @Autowired
  CurrentPolicyProvider currentPolicyProviderMock;

  @Test
  void shouldReturnCurrentModel() throws Exception {
    when(currentStrategyProviderMock.getCurrentStrategy()).thenReturn(of(CURRENT_STRATEGY_NAME));
    when(currentPolicyProviderMock.getCurrentPolicy()).thenReturn(of(CURRENT_POLICY_NAME));

    SolvingModel solvingModel = defaultModelQuery.get();

    assertThat(solvingModel.getStrategyName()).isEqualTo(CURRENT_STRATEGY_NAME);
    assertThat(solvingModel.getPolicyName()).isEqualTo(CURRENT_POLICY_NAME);
  }
}
