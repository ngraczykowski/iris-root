package com.silenteight.simulator.management.statistics;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class SimulationStatisticsConfiguration {

  @Bean
  StaticSimulationStatisticsService simulationStatisticsService() {
    return new StaticSimulationStatisticsService();
  }
}
