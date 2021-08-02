package com.silenteight.warehouse.report.statistics.simulation;

import com.silenteight.warehouse.report.statistics.domain.dto.StatisticsDto;

public interface SimulationStatisticsQuery {

  StatisticsDto getStatistics(String analysis);
}
