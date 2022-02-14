package com.silenteight.payments.bridge.firco.datasource.service.process.agent;

import com.silenteight.payments.bridge.agents.port.CreateContextualLearningFeatureInputUseCase;

import org.springframework.stereotype.Component;

import static com.silenteight.payments.bridge.common.app.AgentsUtils.CONTEXTUAL_LEARNING_DISC_TP;
import static com.silenteight.payments.bridge.common.app.AgentsUtils.CONTEXTUAL_LEARNING_FEATURE_NAME_TP;
import static com.silenteight.payments.bridge.common.app.AgentsUtils.CONTEXTUAL_LEARNING_FEATURE_TP;

@Component
class ContextualLearningTruePositiveFeature extends ContextualLearningFeatureProcess {

  public ContextualLearningTruePositiveFeature(
      CreateContextualLearningFeatureInputUseCase createFeatureInput) {
    super(createFeatureInput);
  }

  @Override
  protected String getFeature() {
    return CONTEXTUAL_LEARNING_FEATURE_TP;
  }

  @Override
  protected String getFeatureName() {
    return CONTEXTUAL_LEARNING_FEATURE_NAME_TP;
  }

  @Override
  protected String getDiscriminator() {
    return CONTEXTUAL_LEARNING_DISC_TP;
  }
}
