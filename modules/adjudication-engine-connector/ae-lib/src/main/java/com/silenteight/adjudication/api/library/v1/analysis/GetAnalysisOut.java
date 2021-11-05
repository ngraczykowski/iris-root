package com.silenteight.adjudication.api.library.v1.analysis;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class GetAnalysisOut {

  long alertsCount;
  long pendingAlerts;

  public boolean hasZeroAlertsCompleted() {
    return alertsCount == pendingAlerts;
  }

  public boolean hasNoPendingAlerts() {
    return pendingAlerts == 0;
  }
}
