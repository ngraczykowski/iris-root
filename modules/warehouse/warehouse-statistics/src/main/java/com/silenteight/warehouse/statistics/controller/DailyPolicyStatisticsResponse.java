package com.silenteight.warehouse.statistics.controller;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
final class DailyPolicyStatisticsResponse {

  private LocalDate day;
  private int alertsCount;
  private int falsePositivesCount;
  private int potentialTruePositivesCount;
  private int manualInvestigationsCount;
  private double efficiencyPercent;
  private double effectivenessPercent;
}
