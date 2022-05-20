package com.silenteight.payments.bridge.firco.events;

import lombok.NonNull;
import lombok.Value;

@Value
public class ManualInvestigationReasonEvent {

  @NonNull
  private String reason;
}
