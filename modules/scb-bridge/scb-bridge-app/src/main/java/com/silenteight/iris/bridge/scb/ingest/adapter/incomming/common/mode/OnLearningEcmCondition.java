/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.mode;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class OnLearningEcmCondition implements Condition {

  private static final String LEARNING_ECM_ENABLED_PROPERTY =
      "silenteight.scb-bridge.learning.ecm.enabled";

  @Override
  public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
    return WorkingModeUtils.isLearningModeEnabled(context) && isLearningEcmEnabled(context);
  }

  private boolean isLearningEcmEnabled(ConditionContext context) {
    return context.getEnvironment()
        .getProperty(LEARNING_ECM_ENABLED_PROPERTY, Boolean.class, false);
  }
}
