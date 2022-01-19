package com.silenteight.warehouse.report.statistics.simulation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class SimulationStatisticsConfiguration {

  @Bean
  GetSimulationStatisticsUseCase getSimulationStatisticsUseCase(
      SimulationStatisticsQuery statisticsQuery) {

    return new GetSimulationStatisticsUseCase(statisticsQuery);
  }
}
