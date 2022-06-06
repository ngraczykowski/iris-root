package com.silenteight.scb.ingest.adapter.incomming.common.mode;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import static com.silenteight.scb.ingest.adapter.incomming.common.mode.WorkingModeUtils.isLearningModeEnabled;

public class OnLearningAlertCondition implements Condition {

  private static final String LEARNING_ALERT_ENABLED_PROPERTY =
      "silenteight.scb-bridge.learning.alert.enabled";

  @Override
  public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
    return isLearningModeEnabled(context) && isLearningAlertEnabled(context);
  }

  private boolean isLearningAlertEnabled(ConditionContext context) {
    return context.getEnvironment()
        .getProperty(LEARNING_ALERT_ENABLED_PROPERTY, Boolean.class, false);
  }
}
