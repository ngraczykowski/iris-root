package com.silenteight.payments.bridge.datasource.agent;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.payments.bridge.agents.model.ContextualLearningAgentRequest;
import com.silenteight.payments.bridge.agents.port.CreateContextualLearningFeatureInputUseCase;
import com.silenteight.payments.bridge.common.protobuf.AgentDataSourceUtils;
import com.silenteight.payments.bridge.datasource.agent.dto.FeatureInputUnstructured;
import com.silenteight.payments.bridge.datasource.agent.dto.FeatureInputUnstructured.ContextualAgentData;

@RequiredArgsConstructor
abstract class ContextualAgentFactory extends BaseFeatureInputUnstructuredFactory {

  private final CreateContextualLearningFeatureInputUseCase createFeatureInput;

  @Override
  protected FeatureInput createFeatureInput(FeatureInputUnstructured featureInputUnstructured) {

    var contextualAgentData = featureInputUnstructured.getContextualAgentData();
    var request = createContextualAgentRequest(contextualAgentData);
    var featureInput = createFeatureInput.create(request);
    return AgentDataSourceUtils.createFeatureInput(getFeatureName(), featureInput);
  }

  private ContextualLearningAgentRequest createContextualAgentRequest(
      ContextualAgentData contextualAgentData) {
    return ContextualLearningAgentRequest.builder()
        .feature(getFeatureName())
        .ofacId(contextualAgentData.getOfacId())
        .watchlistType(contextualAgentData.getWatchlistType())
        .matchingField(contextualAgentData.getMatchingField())
        .matchText(contextualAgentData.getMatchText())
        .discriminator(getDiscriminator())
        .build();
  }

  protected abstract String getFeatureName();

  protected abstract String getDiscriminator();

}
