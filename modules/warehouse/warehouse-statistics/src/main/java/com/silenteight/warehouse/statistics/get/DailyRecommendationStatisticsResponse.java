package com.silenteight.warehouse.statistics.get;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
final class DailyRecommendationStatisticsResponse {

  private LocalDate day;
  private int alertsCount;
  private int falsePositivesCount;
  private int potentialTruePositivesCount;
  private int manualInvestigationsCount;
  private int analystDecisionCount;
  private Double efficiencyPercent;
  private Double effectivenessPercent;
}
