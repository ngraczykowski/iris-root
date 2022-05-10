package com.silenteight.scb.ingest.adapter.incomming.common.mode;

import lombok.NoArgsConstructor;

import org.springframework.context.annotation.ConditionContext;

import static lombok.AccessLevel.PRIVATE;
import static org.apache.commons.lang3.StringUtils.isBlank;

@NoArgsConstructor(access = PRIVATE)
class WorkingModeUtils {

  private static final String SILENTEIGHT_SCB_BRIDGE_WORKING_MODE =
      "silenteight.scb-bridge.working-mode";

  static boolean isOnlyRealTimeModeEnabled(ConditionContext context) {
    String value = context.getEnvironment().getProperty(SILENTEIGHT_SCB_BRIDGE_WORKING_MODE);
    return !isBlank(value) && ScbBridgeMode.REAL_TIME_ONLY.name().equalsIgnoreCase(value);
  }
}
