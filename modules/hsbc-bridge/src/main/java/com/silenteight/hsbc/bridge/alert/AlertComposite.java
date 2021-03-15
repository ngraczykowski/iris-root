package com.silenteight.hsbc.bridge.alert;

import lombok.Builder;
import lombok.Value;

import com.silenteight.hsbc.bridge.rest.model.input.Alert;
import com.silenteight.hsbc.bridge.rest.model.input.AlertSystemInformation;

@Builder
@Value
public class AlertComposite {

  long id;
  Alert alert;

  public AlertSystemInformation getAlertSystemInformation() {
    return alert.getSystemInformation();
  }
}
