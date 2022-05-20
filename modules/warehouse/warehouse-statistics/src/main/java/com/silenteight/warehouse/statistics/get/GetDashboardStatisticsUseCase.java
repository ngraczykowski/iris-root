package com.silenteight.warehouse.statistics.get;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class GetDashboardStatisticsUseCase {

  @NonNull
  private final DailyRecommendationStatisticsRepository repository;


  public StatisticsResponse getTotalCount(StatisticsRequest request) {

    log.info("Statistics request received, calculation period: from={}, to={}",
        request.getFrom(), request.getTo());

    List<DailyRecommendationStatistics> statistics =
        repository.findByDayBetweenOrderByDayDesc(request.getFrom(), request.getTo());

    double alertsCount =
        getTotalCount(statistics, DailyRecommendationStatistics::getAlertsCount);
    double analystDecisionCount =
        getTotalCount(statistics, DailyRecommendationStatistics::getAnalystDecisionCount);
    double fpCount =
        getTotalCount(statistics, DailyRecommendationStatistics::getFalsePositivesCount);
    double ptpCount =
        getTotalCount(statistics, DailyRecommendationStatistics::getPotentialTruePositivesCount);
    double miCount =
        getTotalCount(statistics, DailyRecommendationStatistics::getManualInvestigationsCount);

    return StatisticsResponse.builder()
        .totalAlerts(asInt(alertsCount))
        .falsePositive(asInt(fpCount))
        .falsePositivePercent(asPercent(divide(fpCount, alertsCount)))
        .potentialTruePositive(asInt(ptpCount))
        .potentialTruePositivePercent(asPercent(divide(ptpCount, alertsCount)))
        .manualInvestigation(asInt(miCount))
        .manualInvestigationPercent(asPercent(divide(miCount, alertsCount)))
        .avgEfficiencyPercent(calculateEfficiency(statistics, alertsCount))
        .avgEffectivenessPercent(calculateEffectiveness(statistics, analystDecisionCount))
        .build();
  }

  public List<DailyRecommendationStatisticsResponse> getTotalDailyCount(StatisticsRequest request) {

    log.info("Daily statistics request received, calculation period: from={}, to={}",
        request.getFrom(), request.getTo());

    return repository.findByDayBetweenOrderByDayDesc(request.getFrom(), request.getTo())
        .stream()
        .map(GetDashboardStatisticsUseCase::toDailyStatisticsResponse)
        .collect(Collectors.toList());
  }

  private static double getTotalCount(
      List<DailyRecommendationStatistics> dailyRecommendationStatistics,
      ToDoubleFunction<DailyRecommendationStatistics> argumentProvider) {

    return dailyRecommendationStatistics
        .stream()
        .map(argumentProvider::applyAsDouble)
        .reduce(0.0, Double::sum);
  }

  private static double calculateEfficiency(
      List<DailyRecommendationStatistics> statistics,
      double alertsCount) {

    double numeratorTotal =
        getTotalCount(
            statistics,
            GetDashboardStatisticsUseCase::efficiencyWeightedAvgPart);

    return divide(numeratorTotal, alertsCount);
  }

  private static double calculateEffectiveness(
      List<DailyRecommendationStatistics> statistics,
      double analyticsDecisionCount) {

    double numeratorTotal =
        getTotalCount(
            statistics,
            GetDashboardStatisticsUseCase::effectivenessWeightedAvgPart);
    return divide(numeratorTotal, analyticsDecisionCount);
  }

  private static double efficiencyWeightedAvgPart(DailyRecommendationStatistics stat) {
    if (stat.getAlertsCount() == 0) {
      return 0.0;
    }
    return stat.getAlertsCount() * stat.getEfficiencyPercent();
  }

  private static double effectivenessWeightedAvgPart(DailyRecommendationStatistics stat) {
    if (stat.getAnalystDecisionCount() == 0) {
      return 0.0;
    }
    return stat.getAnalystDecisionCount() * stat.getEffectivenessPercent();
  }

  private static Double divide(Double numerator, Double denominator) {
    return numerator / denominator;
  }

  private static Double asPercent(Double num) {
    return num * 100.0;
  }

  private static int asInt(double num) {
    return (int) Math.round(num);
  }

  private static DailyRecommendationStatisticsResponse toDailyStatisticsResponse(
      DailyRecommendationStatistics statistics) {
    return DailyRecommendationStatisticsResponse.builder()
        .alertsCount(statistics.getAlertsCount())
        .day(statistics.getDay())
        .falsePositivesCount(statistics.getFalsePositivesCount())
        .potentialTruePositivesCount(statistics.getPotentialTruePositivesCount())
        .manualInvestigationsCount(statistics.getManualInvestigationsCount())
        .analystDecisionCount(statistics.getAnalystDecisionCount())
        .effectivenessPercent(statistics.getEffectivenessPercent())
        .efficiencyPercent(statistics.getEfficiencyPercent())
        .build();
  }
}
