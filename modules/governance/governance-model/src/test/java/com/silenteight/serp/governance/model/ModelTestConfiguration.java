package com.silenteight.serp.governance.model;

import com.silenteight.serp.governance.policy.current.CurrentPolicyProvider;
import com.silenteight.serp.governance.strategy.CurrentStrategyProvider;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = {
    ModelModule.class
})
public class ModelTestConfiguration {

  @MockBean
  CurrentStrategyProvider currentStrategyProviderMock;

  @MockBean
  CurrentPolicyProvider currentPolicyProviderMock;
}
