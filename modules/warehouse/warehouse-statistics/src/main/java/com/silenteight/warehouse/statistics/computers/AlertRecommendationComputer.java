package com.silenteight.warehouse.statistics.computers;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.common.domain.Recommendation;
import com.silenteight.warehouse.common.domain.RecommendationMapper;
import com.silenteight.warehouse.indexer.alert.dto.AlertDto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.silenteight.warehouse.common.domain.Recommendation.ACTION_FALSE_POSITIVE;
import static com.silenteight.warehouse.common.domain.Recommendation.ACTION_MANUAL_INVESTIGATION;
import static com.silenteight.warehouse.common.domain.Recommendation.ACTION_POTENTIAL_TRUE_POSITIVE;
import static com.silenteight.warehouse.common.domain.Recommendation.UNSPECIFIED;

/**
 * Computer which provides number of Alerts, PTP,TP and Manual Investigation recommendations on an
 * alert sample.
 *
 * <p>Based on the gathered data the efficiencyPercent and efficiencyPercent is calculated
 */
@AllArgsConstructor
@Slf4j
public final class AlertRecommendationComputer
    implements StatisticsComputer<AlertDto, AlertsRecommendationStatistics> {

  @NonNull
  private final RecommendationMapper mapper;

  private static int getRecommendationCount(
      Map<Recommendation, Long> result, Recommendation recommendation) {
    return Math.toIntExact(result.getOrDefault(recommendation, 0L));
  }

  private static Double calculateEfficiency(
      List<AlertDto> alerts,
      Map<Recommendation, Long> result) {

    var potentialTruePositivesCount =
        getRecommendationCount(result, ACTION_POTENTIAL_TRUE_POSITIVE);
    var falsePositivesCount = getRecommendationCount(result, ACTION_FALSE_POSITIVE);

    return alerts.isEmpty() ? null :
           ((double) (potentialTruePositivesCount + falsePositivesCount) / alerts.size()) * 100;
  }

  @Override
  public AlertsRecommendationStatistics compute(
      List<AlertDto> alerts) {

    Map<Recommendation, Long> result = alerts.stream()
        .collect(Collectors.groupingBy(
            alert -> mapper.getRecommendationByValue(alert.getPayload()),
            Collectors.counting()));

    var unspecifiedRecommendationCount = getRecommendationCount(result, UNSPECIFIED);
    if (unspecifiedRecommendationCount > 0) {
      log.warn(
          "Some alerts have no recommendations, alertCountWithoutRecommendation={}",
          unspecifiedRecommendationCount);
    }

    return AlertsRecommendationStatistics.builder()
        .alertsCount(alerts.size())
        .potentialTruePositivesCount(getRecommendationCount(result, ACTION_POTENTIAL_TRUE_POSITIVE))
        .falsePositivesCount(getRecommendationCount(result, ACTION_FALSE_POSITIVE))
        .manualInvestigationsCount(getRecommendationCount(result, ACTION_MANUAL_INVESTIGATION))
        // TODO(tdrozdz): This has to be additionally implemented as to do it analytics decision is
        // required
        .effectivenessPercent(null)
        .efficiencyPercent(calculateEfficiency(alerts, result)).build();
  }
}
