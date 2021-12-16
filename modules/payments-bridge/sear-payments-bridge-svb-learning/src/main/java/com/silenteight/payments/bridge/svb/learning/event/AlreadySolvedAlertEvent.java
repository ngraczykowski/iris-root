package com.silenteight.payments.bridge.svb.learning.event;

import lombok.NonNull;
import lombok.Value;

@Value
public class AlreadySolvedAlertEvent {
  @NonNull
  private long counter;
}
