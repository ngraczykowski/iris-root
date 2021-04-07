package com.silenteight.hsbc.bridge.alert;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class AlertComposite {

  long id;
  int caseId;
  AlertRawData alertRawData;
}
