package com.silenteight.warehouse.statistics.persistance;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.statistics.computers.AlertsRecommendationStatistics;
import com.silenteight.warehouse.statistics.get.DailyRecommendationStatistics;
import com.silenteight.warehouse.statistics.get.DailyRecommendationStatisticsRepository;

import com.google.common.collect.Range;

import java.time.LocalDate;
import java.util.Optional;

@RequiredArgsConstructor
public final class DailyRecommendationPersistence
    implements DataPersister<AlertsRecommendationStatistics> {

  @NonNull
  private final DailyRecommendationStatisticsRepository dailyStatisticRepository;

  @Override
  public void save(AlertsRecommendationStatistics data, Range<LocalDate> range) {
    var dailyBuilder = DailyRecommendationStatistics.builder()
        .day(range.upperEndpoint())
        .alertsCount(data.getAlertsCount())
        .falsePositivesCount(data.getFalsePositivesCount())
        .potentialTruePositivesCount(data.getPotentialTruePositivesCount())
        .manualInvestigationsCount(data.getManualInvestigationsCount())
        .effectivenessPercent(data.getEffectivenessPercent())
        .efficiencyPercent(data.getEfficiencyPercent());
    dailyStatisticRepository.save(dailyBuilder.build());
  }

  @Override
  public Optional<Range<LocalDate>> getLastPersistedRange(Number value) {
    return dailyStatisticRepository.findFirstByOrderByDayWithOffset(value)
        .map((v -> Range.closed(v.getDay(), v.getDay())));
  }
}
