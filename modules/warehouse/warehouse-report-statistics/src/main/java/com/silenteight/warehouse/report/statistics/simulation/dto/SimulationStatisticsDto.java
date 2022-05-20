package com.silenteight.warehouse.report.statistics.simulation.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class SimulationStatisticsDto {

  @NonNull
  EfficiencyDto efficiency;
  @NonNull
  EffectivenessDto effectiveness;

  @Value
  @Builder
  public static class EffectivenessDto {

    long analystSolvedAsFalsePositive;
    long aiSolvedAsFalsePositive;
  }

  @Value
  @Builder
  public static class EfficiencyDto {

    long solvedAlerts;
    long allAlerts;
  }
}
