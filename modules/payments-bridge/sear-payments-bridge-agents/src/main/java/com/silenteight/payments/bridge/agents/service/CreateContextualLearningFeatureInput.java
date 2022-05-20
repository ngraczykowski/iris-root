package com.silenteight.payments.bridge.agents.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.api.historicaldecisions.v2.HistoricalDecisionsFeatureInput;
import com.silenteight.datasource.api.historicaldecisions.v2.ModelKey;
import com.silenteight.payments.bridge.agents.model.ContextualLearningAgentRequest;
import com.silenteight.payments.bridge.agents.port.CreateContextualLearningFeatureInputUseCase;
import com.silenteight.payments.bridge.common.agents.AlertedPartyIdContextAdapter;
import com.silenteight.payments.bridge.common.agents.ContextualAlertedPartyIdModel;
import com.silenteight.payments.bridge.common.agents.ContextualLearningProperties;
import com.silenteight.payments.bridge.common.app.LearningProperties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties({ LearningProperties.class, ContextualLearningProperties.class })
class CreateContextualLearningFeatureInput implements CreateContextualLearningFeatureInputUseCase {

  private final LearningProperties learningProperties;
  private final ContextualLearningProperties contextualLearningProperties;

  @Override
  public HistoricalDecisionsFeatureInput create(ContextualLearningAgentRequest request) {
    return HistoricalDecisionsFeatureInput.newBuilder()
        .setFeature(request.getFeature())
        .setModelKey(createModelKey(request))
        .setDiscriminator(request.createDiscriminator(learningProperties.getDiscriminatorPrefix()))
        .build();
  }

  private ModelKey createModelKey(ContextualLearningAgentRequest request) {
    var alertedPartyId = getAlertedPartyId(request);
    return request.createModelKey(alertedPartyId);
  }

  private String getAlertedPartyId(ContextualLearningAgentRequest request) {
    var contextualModel = createContextualModel(request);
    return AlertedPartyIdContextAdapter.generateAlertedPartyId(contextualModel);
  }

  private ContextualAlertedPartyIdModel createContextualModel(
      ContextualLearningAgentRequest request) {
    var contextualModelBuilder
        = contextualLearningProperties.getModelBuilder();
    return contextualModelBuilder
        .matchingField(request.getMatchingField())
        .matchText(request.getMatchText())
        .build();
  }
}
