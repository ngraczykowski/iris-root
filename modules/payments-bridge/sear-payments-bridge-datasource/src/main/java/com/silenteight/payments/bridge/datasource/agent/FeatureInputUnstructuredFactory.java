package com.silenteight.payments.bridge.datasource.agent;

import com.silenteight.datasource.agentinput.api.v1.AgentInput;
import com.silenteight.payments.bridge.datasource.FeatureInputSpecification;
import com.silenteight.payments.bridge.datasource.agent.dto.FeatureInputUnstructured;

interface FeatureInputUnstructuredFactory {

  AgentInput createAgentInput(FeatureInputUnstructured featureInputUnstructured);

  boolean shouldProcess(final FeatureInputSpecification featureInputSpecification);

}
