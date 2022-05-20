package com.silenteight.warehouse.report.statistics.simulation.get;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.report.statistics.simulation.dto.SimulationStatisticsDto;
import com.silenteight.warehouse.report.statistics.simulation.query.StatisticsQuery;

@Slf4j
@RequiredArgsConstructor
class GetSimulationStatisticsUseCase {

  @NonNull
  private final StatisticsQuery statisticsQuery;

  public SimulationStatisticsDto activate(String analysisId) {
    return statisticsQuery.getStatistics(analysisId);
  }
}
