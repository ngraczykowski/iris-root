package com.silenteight.simulator.management.progress;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SimulationProgressDto {

  private long allAlerts;
  private long solvedAlerts;
  private long indexedAlerts;
}
