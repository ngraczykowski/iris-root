/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.mode;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class OnLearningAlertCondition implements Condition {

  private static final String LEARNING_ALERT_ENABLED_PROPERTY =
      "silenteight.scb-bridge.learning.alert.enabled";

  @Override
  public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
    return WorkingModeUtils.isLearningModeEnabled(context) && isLearningAlertEnabled(context);
  }

  private boolean isLearningAlertEnabled(ConditionContext context) {
    return context.getEnvironment()
        .getProperty(LEARNING_ALERT_ENABLED_PROPERTY, Boolean.class, false);
  }
}
