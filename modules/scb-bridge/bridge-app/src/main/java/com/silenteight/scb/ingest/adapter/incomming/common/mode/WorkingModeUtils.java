package com.silenteight.scb.ingest.adapter.incomming.common.mode;

import lombok.NoArgsConstructor;

import org.springframework.context.annotation.ConditionContext;

import java.util.List;

import static com.silenteight.scb.ingest.adapter.incomming.common.mode.ScbBridgeMode.ALL;
import static com.silenteight.scb.ingest.adapter.incomming.common.mode.ScbBridgeMode.LEARNING;
import static com.silenteight.scb.ingest.adapter.incomming.common.mode.ScbBridgeMode.PERIODIC_SOLVING;
import static com.silenteight.scb.ingest.adapter.incomming.common.mode.ScbBridgeMode.REAL_TIME_SOLVING;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
class WorkingModeUtils {

  private static final String WORKING_MODE_PROPERTY = "silenteight.scb-bridge.working-mode";

  static boolean isRealTimeSolvingModeEnabled(ConditionContext context) {
    var values = context.getEnvironment().getProperty(WORKING_MODE_PROPERTY, List.class);
    return containsAny(values, List.of(REAL_TIME_SOLVING.name(), ALL.name()));
  }

  static boolean isPeriodicSolvingModeEnabled(ConditionContext context) {
    var values = context.getEnvironment().getProperty(WORKING_MODE_PROPERTY, List.class);
    return containsAny(values, List.of(PERIODIC_SOLVING.name(), ALL.name()));
  }

  static boolean isLearningModeEnabled(ConditionContext context) {
    var values = context.getEnvironment().getProperty(WORKING_MODE_PROPERTY, List.class);
    return containsAny(values, List.of(LEARNING.name(), ALL.name()));
  }

  private static boolean containsAny(List<String> properties, List<String> expected) {
    if (properties == null || expected == null) return false;
    return properties.stream()
        .map(String::toUpperCase)
        .anyMatch(expected::contains);
  }
}
