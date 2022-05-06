package com.silenteight.warehouse.statistics.computers;

import lombok.Builder;
import lombok.Value;

/**
 * Statistics for alerts distinguished by s8 recommendation.
 */
@Value
@Builder
public class AlertsRecommendationStatistics {

  int alertsCount;

  int falsePositivesCount;

  int potentialTruePositivesCount;

  int manualInvestigationsCount;

  int analyticsDecisionCount;

  Double efficiencyPercent;

  Double effectivenessPercent;
}
