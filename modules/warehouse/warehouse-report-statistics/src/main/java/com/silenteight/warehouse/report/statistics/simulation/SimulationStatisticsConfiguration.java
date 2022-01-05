package com.silenteight.warehouse.report.statistics.simulation;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class SimulationStatisticsConfiguration {

  @Bean
  @ConditionalOnProperty("warehouse.report.statistics")
  GetSimulationStatisticsUseCase getSimulationStatisticsUseCase(
      SimulationStatisticsQuery statisticsQuery) {

    return new GetSimulationStatisticsUseCase(statisticsQuery);
  }
}
