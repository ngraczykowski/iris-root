package com.silenteight.payments.bridge.common.event;

import lombok.Builder;
import lombok.Value;

/**
 * Signals finishing alert registration process from learning origin.
 */
@Builder
@Value
public class LearningAlertRegisteredEvent {

  String systemId;

  String alertName;
}
