package com.silenteight.warehouse.report.statistics.simulation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.report.statistics.domain.dto.StatisticsDto;

@Slf4j
@RequiredArgsConstructor
public class GetSimulationStatisticsUseCase {

  @NonNull
  private final SimulationStatisticsQuery statisticsQuery;

  public StatisticsDto activate(String analysisId) {
    return statisticsQuery.getStatistics(analysisId);
  }
}
