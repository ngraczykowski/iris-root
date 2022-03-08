package com.silenteight.payments.bridge.svb.learning.step.etl.feature.service;

import com.silenteight.datasource.agentinput.api.v1.AgentInput;

public enum DefaultFeatureInputSpecification implements FeatureInputSpecification {

  INSTANCE;

  @Override
  public boolean isSatisfy(
      AgentInput agentInput) {
    return true;
  }

}
