package com.silenteight.warehouse.statistics.get;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.MathContext;
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
        repository.findByDayBetween(request.getFrom(), request.getTo());

    double alertsCount =
        getTotalCount(statistics, DailyRecommendationStatistics::getAlertsCount);
    double analystCount =
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
        .avgEfficiencyPercent(calculateEfficiency(statistics, analystCount))
        .avgEffectivenessPercent(calculateEffectiveness(statistics, alertsCount))
        .build();
  }

  public List<DailyRecommendationStatisticsResponse> getTotalDailyCount(StatisticsRequest request) {

    log.info("Daily statistics request received, calculation period: from={}, to={}",
        request.getFrom(), request.getTo());

    return repository.findByDayBetween(request.getFrom(), request.getTo())
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
            stat -> (stat.getAlertsCount() * stat.getEffectivenessPercent()));

    return divide(BigDecimal.valueOf(numeratorTotal), BigDecimal.valueOf(alertsCount));
  }

  private static double calculateEffectiveness(
      List<DailyRecommendationStatistics> statistics,
      double alertsCount) {

    double numeratorTotal =
        getTotalCount(
            statistics,
            stat -> (stat.getAnalystDecisionCount() * stat.getEffectivenessPercent()));

    return divide(BigDecimal.valueOf(numeratorTotal), BigDecimal.valueOf(alertsCount));
  }

  private static double divide(double numerator, double denominator) {
    return divide(BigDecimal.valueOf(numerator), BigDecimal.valueOf(denominator));
  }

  private static double divide(BigDecimal numerator, BigDecimal denominator) {
    return numerator.divide(denominator, new MathContext(4)).doubleValue();
  }

  private static double asPercent(double num) {
    double percent = num * 100.0;
    return Math.round(percent * 100.0) / 100.0;
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
