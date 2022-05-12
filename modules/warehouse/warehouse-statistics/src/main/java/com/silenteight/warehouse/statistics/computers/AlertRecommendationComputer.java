package com.silenteight.warehouse.statistics.computers;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.common.domain.AnalystDecision;
import com.silenteight.warehouse.common.domain.AnalystDecisionMapper;
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

  private static final List<Recommendation> RECOMMENDATION_REQUIRED_FOR_EFFICIENCY =
      List.of(ACTION_FALSE_POSITIVE, ACTION_POTENTIAL_TRUE_POSITIVE);
  @NonNull
  private final RecommendationMapper recommendationMapper;

  @NonNull
  private final AnalystDecisionMapper analystDecisionMapper;

  private static int getRecommendationCount(
      Map<Recommendation, Long> result, Recommendation recommendation) {
    return Math.toIntExact(result.getOrDefault(recommendation, 0L));
  }

  @Override
  public AlertsRecommendationStatistics compute(
      List<AlertDto> alerts) {

    Map<Recommendation, Long> recommendationTypeCounter = alerts.stream()
        .collect(Collectors.groupingBy(
            alert -> recommendationMapper.getRecommendationByValue(alert.getPayload()),
            Collectors.counting()));

    List<AlertDto> analyticsDecisionAlerts = alerts
        .stream()
        .filter(this::shouldBeTakenInToAccountToCalculateEffectiveness)
        .collect(Collectors.toList());

    log.info("Alerts with analyst recommendation {}", analyticsDecisionAlerts.stream().map(
        AlertDto::getDiscriminator).collect(Collectors.toList()));

    var unspecifiedRecommendationCount =
        getRecommendationCount(recommendationTypeCounter, UNSPECIFIED);
    if (unspecifiedRecommendationCount > 0) {
      log.warn(
          "Some alerts have no recommendations, alertCountWithoutRecommendation={}",
          unspecifiedRecommendationCount);
    }

    return AlertsRecommendationStatistics.builder()
        .alertsCount(alerts.size())
        .potentialTruePositivesCount(
            getRecommendationCount(recommendationTypeCounter, ACTION_POTENTIAL_TRUE_POSITIVE))
        .falsePositivesCount(
            getRecommendationCount(recommendationTypeCounter, ACTION_FALSE_POSITIVE))
        .manualInvestigationsCount(
            getRecommendationCount(recommendationTypeCounter, ACTION_MANUAL_INVESTIGATION))
        .effectivenessPercent(calculateEffectiveness(analyticsDecisionAlerts))
        .analyticsDecisionCount(analyticsDecisionAlerts.size())
        .efficiencyPercent(calculateEfficiency(alerts, recommendationTypeCounter)).build();
  }

  private Double calculateEfficiency(
      List<AlertDto> alerts,
      Map<Recommendation, Long> result) {

    var potentialTruePositivesCount =
        getRecommendationCount(result, ACTION_POTENTIAL_TRUE_POSITIVE);
    var falsePositivesCount = getRecommendationCount(result, ACTION_FALSE_POSITIVE);

    return alerts.isEmpty() ? Double.NaN :
           ((double) (potentialTruePositivesCount + falsePositivesCount) / alerts.size()) * 100;
  }

  private Double calculateEffectiveness(
      List<AlertDto> alerts) {
    if (alerts.isEmpty()) {
      return Double.NaN;
    }
    var analystDecisionFalsePositive = alerts
        .stream()
        .filter(this::isProperlyResolved)
        .count();
    return ((double) analystDecisionFalsePositive / alerts.size()) * 100;
  }

  private boolean shouldBeTakenInToAccountToCalculateEffectiveness(AlertDto alertDto) {
    var analystDecisionFieldName = analystDecisionMapper.getAnalystDecisionFieldName();
    if (!alertDto.getPayload().containsKey(analystDecisionFieldName)) {
      return false;
    }
    return !alertDto.getPayload().get(analystDecisionFieldName).isEmpty()
        && RECOMMENDATION_REQUIRED_FOR_EFFICIENCY.contains(getRecommendationFromAlert(alertDto));

  }

  private boolean isProperlyResolved(AlertDto alertDto) {
    return getAnalystDecisionFromAlert(alertDto) == AnalystDecision.FALSE_POSITIVE
        && getRecommendationFromAlert(alertDto) == ACTION_FALSE_POSITIVE;
  }

  private Recommendation getRecommendationFromAlert(AlertDto alertDto) {
    return recommendationMapper.getRecommendationByValue(alertDto.getPayload());
  }

  private AnalystDecision getAnalystDecisionFromAlert(AlertDto alertDto) {
    return analystDecisionMapper.getAnalystDecisionByValue(alertDto.getPayload());
  }
}
