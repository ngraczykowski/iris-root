package com.silenteight.scb.ingest.adapter.incomming.common.mode;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import static com.silenteight.scb.ingest.adapter.incomming.common.mode.WorkingModeUtils.isLearningModeEnabled;

public class OnLearningEcmCondition implements Condition {

  private static final String LEARNING_ECM_ENABLED_PROPERTY =
      "silenteight.scb-bridge.learning.ecm.enabled";

  @Override
  public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
    return isLearningModeEnabled(context) && isLearningEcmEnabled(context);
  }

  private boolean isLearningEcmEnabled(ConditionContext context) {
    return context.getEnvironment()
        .getProperty(LEARNING_ECM_ENABLED_PROPERTY, Boolean.class, false);
  }
}
