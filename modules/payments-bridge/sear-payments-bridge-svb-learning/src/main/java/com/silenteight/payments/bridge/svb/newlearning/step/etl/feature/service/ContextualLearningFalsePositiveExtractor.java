package com.silenteight.payments.bridge.svb.newlearning.step.etl.feature.service;

import com.silenteight.payments.bridge.agents.port.CreateContextualLearningFeatureInputUseCase;

import org.springframework.stereotype.Service;

import static com.silenteight.payments.bridge.common.app.AgentsUtils.CONTEXTUAL_LEARNING_DISC_FP;
import static com.silenteight.payments.bridge.common.app.AgentsUtils.CONTEXTUAL_LEARNING_FEATURE_FP;
import static com.silenteight.payments.bridge.common.app.AgentsUtils.CONTEXTUAL_LEARNING_FEATURE_NAME_FP;

@Service
class ContextualLearningFalsePositiveExtractor extends ContextualLearningFeatureExtractorService {

  public ContextualLearningFalsePositiveExtractor(
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
