package com.silenteight.serp.governance.model;

import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.serp.governance.model.provide.grpc.PolicyFeatureProvider;
import com.silenteight.serp.governance.policy.current.CurrentPolicyProvider;
import com.silenteight.serp.governance.policy.promote.PromotePolicyUseCase;
import com.silenteight.serp.governance.policy.step.logic.PolicyStepsFeaturesProvider;
import com.silenteight.serp.governance.policy.transfer.importing.ImportPolicyUseCase;
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
  AuditingLogger auditingLogger;

  @MockBean
  CurrentStrategyProvider currentStrategyProviderMock;

  @MockBean
  CurrentPolicyProvider currentPolicyProviderMock;

  @MockBean
  PolicyStepsFeaturesProvider policyStepsFeaturesProvider;

  @MockBean
  PolicyFeatureProvider policyFeatureProvider;

  @MockBean
  PromotePolicyUseCase promotePolicyUseCase;

  @MockBean
  ImportPolicyUseCase importPolicyUseCase;
}
