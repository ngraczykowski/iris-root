package com.silenteight.payments.bridge.datasource.agent;

import com.silenteight.datasource.agentinput.api.v1.AgentInput;
import com.silenteight.payments.bridge.datasource.FeatureInputSpecification;
import com.silenteight.payments.bridge.datasource.agent.dto.FeatureInputStructured;

interface FeatureInputStructuredFactory {

  AgentInput createAgentInput(FeatureInputStructured featureInputStructured);

  boolean shouldProcess(final FeatureInputSpecification featureInputSpecification);

}
