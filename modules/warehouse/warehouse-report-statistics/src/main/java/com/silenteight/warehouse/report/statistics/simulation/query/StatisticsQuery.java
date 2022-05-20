package com.silenteight.warehouse.report.statistics.simulation.query;

import com.silenteight.warehouse.report.statistics.simulation.dto.SimulationStatisticsDto;

public interface StatisticsQuery {

  SimulationStatisticsDto getStatistics(String analysis);
}
