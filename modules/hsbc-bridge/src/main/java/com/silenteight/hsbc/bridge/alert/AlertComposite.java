package com.silenteight.hsbc.bridge.alert;

import lombok.Builder;
import lombok.Value;

import com.silenteight.hsbc.bridge.bulk.rest.input.Alert;
import com.silenteight.hsbc.bridge.bulk.rest.input.AlertSystemInformation;

@Builder
@Value
public class AlertComposite {

  long id;
  int caseId;
  Alert alert;

  public AlertSystemInformation getAlertSystemInformation() {
    return alert.getSystemInformation();
  }
}
