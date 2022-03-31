package com.silenteight.warehouse.statistics.controller;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
final class PolicyStatisticsResponse {

  private double avgEffectivenessPercent;
  private double avgEfficiencyPercent;
  private int avgResolutionPerDay;
  private int totalAlerts;
  private int falsePositive;
  private double falsePositivePercent;
  private int potentialTruePositive;
  private double potentialTruePositivePercent;
  private int manualInvestigation;
  private double manualInvestigationPercent;
}
