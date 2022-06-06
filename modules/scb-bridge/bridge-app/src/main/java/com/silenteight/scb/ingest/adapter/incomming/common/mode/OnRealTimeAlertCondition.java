package com.silenteight.scb.ingest.adapter.incomming.common.mode;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import static com.silenteight.scb.ingest.adapter.incomming.common.mode.WorkingModeUtils.isRealTimeSolvingModeEnabled;

public class OnRealTimeAlertCondition implements Condition {

  @Override
  public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
    return isRealTimeSolvingModeEnabled(context);
  }
}
