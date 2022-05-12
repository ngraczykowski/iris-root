package com.silenteight.warehouse.report.statistics.simulation.get;

import com.silenteight.warehouse.report.statistics.simulation.query.StatisticsQuery;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class SimulationStatisticsConfiguration {

  @Bean
  GetSimulationStatisticsUseCase getSimulationStatisticsUseCase(
      StatisticsQuery statisticsQuery) {

    return new GetSimulationStatisticsUseCase(statisticsQuery);
  }
}
