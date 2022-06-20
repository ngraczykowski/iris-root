/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.mode;

import lombok.NoArgsConstructor;

import org.springframework.context.annotation.ConditionContext;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
class WorkingModeUtils {

  private static final String WORKING_MODE_PROPERTY = "silenteight.scb-bridge.working-mode";

  static boolean isRealTimeSolvingModeEnabled(ConditionContext context) {
    var values = context.getEnvironment().getProperty(WORKING_MODE_PROPERTY, List.class);
    return containsAny(values, List.of(ScbBridgeMode.REAL_TIME_SOLVING.name(), ScbBridgeMode.ALL.name()));
  }

  static boolean isPeriodicSolvingModeEnabled(ConditionContext context) {
    var values = context.getEnvironment().getProperty(WORKING_MODE_PROPERTY, List.class);
    return containsAny(values, List.of(ScbBridgeMode.PERIODIC_SOLVING.name(), ScbBridgeMode.ALL.name()));
  }

  static boolean isLearningModeEnabled(ConditionContext context) {
    var values = context.getEnvironment().getProperty(WORKING_MODE_PROPERTY, List.class);
    return containsAny(values, List.of(ScbBridgeMode.LEARNING.name(), ScbBridgeMode.ALL.name()));
  }

  private static boolean containsAny(List<String> properties, List<String> expected) {
    if (properties == null || expected == null) return false;
    return properties.stream()
        .map(String::toUpperCase)
        .anyMatch(expected::contains);
  }
}
