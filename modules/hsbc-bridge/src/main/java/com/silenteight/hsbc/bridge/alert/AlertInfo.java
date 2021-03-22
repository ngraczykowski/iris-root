package com.silenteight.hsbc.bridge.alert;

import lombok.Builder;
import lombok.Value;

import com.silenteight.hsbc.bridge.domain.CasesWithAlertURL;

@Builder
@Value
public class AlertInfo {

  Long id;
  int caseId;
  CasesWithAlertURL casesWithAlertURL;
}
