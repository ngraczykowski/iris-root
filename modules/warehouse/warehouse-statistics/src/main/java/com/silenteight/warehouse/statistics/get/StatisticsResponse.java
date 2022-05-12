package com.silenteight.warehouse.statistics.get;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
final class StatisticsResponse {

  private Double avgEffectivenessPercent;
  private Double avgEfficiencyPercent;
  private int totalAlerts;
  private int falsePositive;
  private Double falsePositivePercent;
  private int potentialTruePositive;
  private Double potentialTruePositivePercent;
  private int manualInvestigation;
  private Double manualInvestigationPercent;

}
