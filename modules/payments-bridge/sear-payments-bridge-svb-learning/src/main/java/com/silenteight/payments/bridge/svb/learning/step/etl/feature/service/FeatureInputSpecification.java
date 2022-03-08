package com.silenteight.payments.bridge.svb.learning.step.etl.feature.service;

import com.silenteight.datasource.agentinput.api.v1.AgentInput;

public interface FeatureInputSpecification {

  boolean isSatisfy(
      AgentInput agentInput);
}
