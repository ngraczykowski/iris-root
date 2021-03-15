package com.silenteight.hsbc.bridge.alert;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.domain.CasesWithAlertURL;

@RequiredArgsConstructor
@Getter
class AlertRawData {

  private final CasesWithAlertURL casesWithAlertURL;

  int getCaseId() {
    return casesWithAlertURL.getId();
  }
}
