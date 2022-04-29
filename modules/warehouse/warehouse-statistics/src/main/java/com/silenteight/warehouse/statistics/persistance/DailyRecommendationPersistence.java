package com.silenteight.warehouse.statistics.persistance;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.statistics.computers.AlertsRecommendationStatistics;
import com.silenteight.warehouse.statistics.model.DailyPolicyStatistics;
import com.silenteight.warehouse.statistics.model.DailyPolicyStatisticsRepository;

import com.google.common.collect.Range;

import java.time.LocalDate;
import java.util.Optional;

@RequiredArgsConstructor
public final class DailyRecommendationPersistence
    implements DataPersister<AlertsRecommendationStatistics> {

  @NonNull
  private final DailyPolicyStatisticsRepository dailyPolicyStatisticRepository;

  @Override
  public void save(AlertsRecommendationStatistics data, Range<LocalDate> range) {
    var dailyBuilder = DailyPolicyStatistics.builder()
        .day(range.upperEndpoint())
        .alertsCount(data.getAlertsCount())
        .falsePositivesCount(data.getFalsePositivesCount())
        .potentialTruePositivesCount(data.getPotentialTruePositivesCount())
        .manualInvestigationsCount(data.getManualInvestigationsCount())
        .effectivenessPercent(data.getEffectivenessPercent())
        .efficiencyPercent(data.getEfficiencyPercent());
    dailyPolicyStatisticRepository.save(dailyBuilder.build());
  }

  @Override
  public Optional<Range<LocalDate>> getLastPersistedRange(Number value) {
    return dailyPolicyStatisticRepository.findFirstByOrderByDayWithOffset(value)
        .map((v -> Range.closed(v.getDay(), v.getDay())));
  }
}
