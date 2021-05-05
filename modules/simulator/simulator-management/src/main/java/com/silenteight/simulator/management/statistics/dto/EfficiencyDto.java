package com.silenteight.simulator.management.statistics.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class EfficiencyDto {

  long solvedAlerts;
  long allAlerts;
}
