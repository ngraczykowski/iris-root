package com.silenteight.payments.bridge.firco.metrics.alert;

import lombok.Value;

import java.util.UUID;

@Value
public class AlertResolutionEndEvent {

  UUID alertId;
  long endTime;
}
