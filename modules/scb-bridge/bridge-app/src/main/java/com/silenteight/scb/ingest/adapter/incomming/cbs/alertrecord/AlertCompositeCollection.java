package com.silenteight.scb.ingest.adapter.incomming.cbs.alertrecord;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
class AlertCompositeCollection {

  private final List<ValidAlertComposite> validAlerts;
  private final List<InvalidAlert> invalidAlerts;
}
