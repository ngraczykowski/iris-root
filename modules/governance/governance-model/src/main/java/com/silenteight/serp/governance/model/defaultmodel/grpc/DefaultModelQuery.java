package com.silenteight.serp.governance.model.defaultmodel.grpc;

import lombok.RequiredArgsConstructor;

import com.silenteight.model.api.v1.SolvingModel;
import com.silenteight.serp.governance.policy.current.CurrentPolicyProvider;
import com.silenteight.serp.governance.strategy.CurrentStrategyProvider;

@RequiredArgsConstructor
public class DefaultModelQuery {

  static final String DEFAULT_MODEL_NAME = "models/default";

  private final CurrentStrategyProvider currentStrategyProvider;
  private final CurrentPolicyProvider currentPolicyProvider;

  public SolvingModel get() throws ModelMisconfiguredException {
    String strategyName = currentStrategyProvider.getCurrentStrategy()
        .orElseThrow(() -> new ModelMisconfiguredException(DEFAULT_MODEL_NAME, "strategyName"));

    String policyName = currentPolicyProvider.getCurrentPolicy()
        .orElseThrow(() -> new ModelMisconfiguredException(DEFAULT_MODEL_NAME, "policyName"));

    return SolvingModel.newBuilder()
        .setStrategyName(strategyName)
        .setPolicyName(policyName)
        .setName(DEFAULT_MODEL_NAME)
        .build();
  }
}
