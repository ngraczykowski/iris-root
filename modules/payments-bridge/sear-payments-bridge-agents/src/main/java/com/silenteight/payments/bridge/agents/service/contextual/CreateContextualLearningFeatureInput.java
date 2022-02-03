package com.silenteight.payments.bridge.agents.service.contextual;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.api.historicaldecisions.v2.Discriminator;
import com.silenteight.datasource.api.historicaldecisions.v2.HistoricalDecisionsFeatureInput;
import com.silenteight.datasource.api.historicaldecisions.v2.ModelKey;
import com.silenteight.payments.bridge.agents.model.ContextualLearningAgentRequest;
import com.silenteight.payments.bridge.agents.port.CreateContextualLearningFeatureInputUseCase;
import com.silenteight.payments.bridge.common.app.LearningProperties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import static com.silenteight.payments.bridge.common.app.AgentsUtils.CONTEXTUAL_LEARNING_DISC;
import static com.silenteight.payments.bridge.common.app.AgentsUtils.CONTEXTUAL_LEARNING_FEATURE_NAME;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(LearningProperties.class)
class CreateContextualLearningFeatureInput implements CreateContextualLearningFeatureInputUseCase {

  private final AlertedPartyIdContextAdapter alertedPartyIdAdapter;
  private final LearningProperties properties;

  @Override
  public HistoricalDecisionsFeatureInput create(ContextualLearningAgentRequest request) {
    return HistoricalDecisionsFeatureInput.newBuilder()
        .setFeature(CONTEXTUAL_LEARNING_FEATURE_NAME)
        .setModelKey(createModelKey(request))
        .setDiscriminator(createDiscriminator())
        .build();
  }

  private ModelKey createModelKey(ContextualLearningAgentRequest request) {
    var alertedPartyId = getAlertedPartyId(request);
    return request.createModelKey(alertedPartyId);
  }

  private String getAlertedPartyId(ContextualLearningAgentRequest request) {
    var matchingField = request.getMatchingField();
    var matchText = request.getMatchText();
    return alertedPartyIdAdapter.generateAlertedPartyId(matchingField, matchText);
  }

  private Discriminator createDiscriminator() {
    return Discriminator.newBuilder()
        .setValue(getDiscriminatorValue())
        .build();
  }

  private String getDiscriminatorValue() {
    return properties.getDiscriminatorPrefix() + "_" + CONTEXTUAL_LEARNING_DISC;
  }
}
