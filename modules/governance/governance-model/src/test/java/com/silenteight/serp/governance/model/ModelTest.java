package com.silenteight.serp.governance.model;

import com.silenteight.model.api.v1.Feature;
import com.silenteight.model.api.v1.SolvingModel;
import com.silenteight.sep.base.testing.BaseDataJpaTest;
import com.silenteight.serp.governance.model.defaultmodel.grpc.DefaultModelQuery;
import com.silenteight.serp.governance.model.defaultmodel.grpc.SolvingModelProvider;
import com.silenteight.serp.governance.policy.current.CurrentPolicyProvider;
import com.silenteight.serp.governance.strategy.CurrentStrategyProvider;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static com.silenteight.serp.governance.model.agent.details.AgentDetailsFixture.AGENT_FEATURE_DATE;
import static com.silenteight.serp.governance.model.agent.details.AgentDetailsFixture.AGENT_FEATURE_NAME;
import static com.silenteight.serp.governance.model.category.CategoryFixture.APTYPE_CATEGORY_NAME;
import static com.silenteight.serp.governance.model.category.CategoryFixture.ISDENY_CATEGORY_NAME;
import static com.silenteight.serp.governance.model.fixture.ModelFixtures.DEFAULT_MODEL_DTO;
import static com.silenteight.serp.governance.policy.current.CurrentPolicyFixture.CURRENT_POLICY_NAME;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@Transactional
@ContextConfiguration(classes = { ModelTestConfiguration.class })
@TestPropertySource("classpath:data-test.properties")
@AutoConfigureJsonTesters
class ModelTest extends BaseDataJpaTest {

  private static final String CURRENT_STRATEGY_NAME = "strategies/USE_ANALYST_SOLUTION";

  @Autowired
  DefaultModelQuery defaultModelQuery;

  @Autowired
  CurrentStrategyProvider currentStrategyProviderMock;

  @Autowired
  CurrentPolicyProvider currentPolicyProviderMock;

  @Autowired
  SolvingModelProvider solvingModelProvider;

  @Test
  void shouldReturnCurrentModel() throws Exception {
    when(currentStrategyProviderMock.getCurrentStrategy()).thenReturn(of(CURRENT_STRATEGY_NAME));
    when(currentPolicyProviderMock.getCurrentPolicy()).thenReturn(of(CURRENT_POLICY_NAME));

    SolvingModel solvingModel = solvingModelProvider.get(DEFAULT_MODEL_DTO);

    assertThat(solvingModel.getStrategyName()).isEqualTo(CURRENT_STRATEGY_NAME);
    assertThat(solvingModel.getPolicyName()).isEqualTo(CURRENT_POLICY_NAME);
    assertThat(solvingModel.getFeaturesList()).extracting(Feature::getName)
        .containsExactlyInAnyOrder(AGENT_FEATURE_NAME, AGENT_FEATURE_DATE);
    assertThat(solvingModel.getCategoriesList())
        .containsExactlyInAnyOrder(APTYPE_CATEGORY_NAME, ISDENY_CATEGORY_NAME);
  }
}
