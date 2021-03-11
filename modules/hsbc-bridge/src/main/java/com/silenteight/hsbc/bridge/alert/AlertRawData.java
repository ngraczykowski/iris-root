package com.silenteight.hsbc.bridge.alert;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.domain.CasesWithAlertURL;

@RequiredArgsConstructor
@Getter
public class AlertRawData {

  private final CasesWithAlertURL casesWithAlertURL;
}
