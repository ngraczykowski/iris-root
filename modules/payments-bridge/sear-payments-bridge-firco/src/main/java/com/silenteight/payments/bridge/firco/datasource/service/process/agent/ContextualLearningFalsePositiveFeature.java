package com.silenteight.payments.bridge.firco.datasource.service.process.agent;

import com.silenteight.payments.bridge.agents.port.CreateContextualLearningFeatureInputUseCase;

import org.springframework.stereotype.Component;

import static com.silenteight.payments.bridge.common.app.AgentsUtils.CONTEXTUAL_LEARNING_DISC_FP;
import static com.silenteight.payments.bridge.common.app.AgentsUtils.CONTEXTUAL_LEARNING_FEATURE_FP;
import static com.silenteight.payments.bridge.common.app.AgentsUtils.CONTEXTUAL_LEARNING_FEATURE_NAME_FP;

@Component
class ContextualLearningFalsePositiveFeature extends ContextualLearningFeatureProcess {

  public ContextualLearningFalsePositiveFeature(
      CreateContextualLearningFeatureInputUseCase createFeatureInput) {
    super(createFeatureInput);
  }

  @Override
  protected String getFeature() {
    return CONTEXTUAL_LEARNING_FEATURE_FP;
  }

  @Override
  protected String getFeatureName() {
    return CONTEXTUAL_LEARNING_FEATURE_NAME_FP;
  }

  @Override
  protected String getDiscriminator() {
    return CONTEXTUAL_LEARNING_DISC_FP;
  }
}
