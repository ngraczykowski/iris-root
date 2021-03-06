package com.silenteight.payments.bridge.datasource.agent;

import com.silenteight.datasource.agentinput.api.v1.AgentInput;
import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.payments.bridge.datasource.FeatureInputSpecification;
import com.silenteight.payments.bridge.datasource.agent.dto.FeatureInputStructured;

abstract class BaseFeatureInputStructuredFactory implements FeatureInputStructuredFactory {

  @Override
  public AgentInput createAgentInput(FeatureInputStructured featureInputStructured) {
    return AgentInput.newBuilder()
        .setAlert(featureInputStructured.getAlertName())
        .setMatch(featureInputStructured.getMatchName())
        .addFeatureInputs(createFeatureInput(featureInputStructured))
        .build();
  }

  @Override
  public boolean shouldProcess(final FeatureInputSpecification featureInputSpecification) {
    return featureInputSpecification.isSatisfy(getFeatureName());
  }

  protected abstract FeatureInput createFeatureInput(FeatureInputStructured featureInputStructured);

  protected abstract String getFeatureName();
}
