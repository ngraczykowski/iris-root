/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.mode;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class OnAlertProcessorCondition implements Condition {

  private static final String ALERT_PROCESSOR_ENABLED_PROPERTY =
      "silenteight.scb-bridge.solving.alert-processor.enabled";

  @Override
  public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
    return WorkingModeUtils.isPeriodicSolvingModeEnabled(context) && isAlertProcessorEnabled(context);
  }

  private boolean isAlertProcessorEnabled(ConditionContext context) {
    return context.getEnvironment()
        .getProperty(ALERT_PROCESSOR_ENABLED_PROPERTY, Boolean.class, true);
  }
}
