package com.silenteight.payments.bridge.firco.metrics.alert;

import lombok.Value;

import java.util.UUID;

@Value
public class AlertResolutionStartEvent {

  UUID alertId;
  long startTime;
}
