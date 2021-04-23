package com.silenteight.hsbc.bridge.analysis.dto;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class GetAnalysisResponseDto {

  long alertsCount;
  long pendingAlerts;

  public boolean hasPendingAlerts() {
    return pendingAlerts > 0;
  }
}
