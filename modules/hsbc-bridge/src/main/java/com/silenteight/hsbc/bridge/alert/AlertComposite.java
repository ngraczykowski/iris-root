package com.silenteight.hsbc.bridge.alert;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class AlertComposite {

  long id;
  AlertRawData alertRawData;

  public String getAlertExternalId() {
    return alertRawData.getAlertExternalId();
  }
}
