package com.silenteight.scb.ingest.adapter.incomming.common.mode;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import static com.silenteight.scb.ingest.adapter.incomming.common.mode.WorkingModeUtils.isLearningModeEnabled;
import static com.silenteight.scb.ingest.adapter.incomming.common.mode.WorkingModeUtils.isPeriodicSolvingModeEnabled;

public class OnOracleCondition implements Condition {

  @Override
  public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
    return isPeriodicSolvingModeEnabled(context) || isLearningModeEnabled(context);
  }
}
