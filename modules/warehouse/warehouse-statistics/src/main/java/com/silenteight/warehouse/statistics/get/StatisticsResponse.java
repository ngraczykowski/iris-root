package com.silenteight.warehouse.statistics.get;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
final class StatisticsResponse {

  private double avgEffectivenessPercent;
  private double avgEfficiencyPercent;
  private int totalAlerts;
  private int falsePositive;
  private double falsePositivePercent;
  private int potentialTruePositive;
  private double potentialTruePositivePercent;
  private int manualInvestigation;
  private double manualInvestigationPercent;

}
