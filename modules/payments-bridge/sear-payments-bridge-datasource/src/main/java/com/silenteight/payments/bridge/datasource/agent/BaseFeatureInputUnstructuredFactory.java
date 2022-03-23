package com.silenteight.payments.bridge.datasource.agent;

import com.silenteight.datasource.agentinput.api.v1.AgentInput;
import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.payments.bridge.datasource.FeatureInputSpecification;
import com.silenteight.payments.bridge.datasource.agent.dto.FeatureInputUnstructured;

abstract class BaseFeatureInputUnstructuredFactory implements FeatureInputUnstructuredFactory {

  @Override
  public AgentInput createAgentInput(FeatureInputUnstructured featureInputUnstructured) {
    return AgentInput.newBuilder()
        .setAlert(featureInputUnstructured.getAlertName())
        .setMatch(featureInputUnstructured.getMatchName())
        .addFeatureInputs(createFeatureInput(featureInputUnstructured))
        .build();
  }

  @Override
  public boolean shouldProcess(final FeatureInputSpecification featureInputSpecification) {
    return featureInputSpecification.isSatisfy(getFeatureName());
  }

  protected abstract FeatureInput createFeatureInput(
      FeatureInputUnstructured featureInputUnstructured);

  protected abstract String getFeatureName();

}
