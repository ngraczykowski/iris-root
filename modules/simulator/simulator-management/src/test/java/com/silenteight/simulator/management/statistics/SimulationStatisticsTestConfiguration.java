package com.silenteight.simulator.management.statistics;

import org.springframework.context.annotation.Bean;

public class SimulationStatisticsTestConfiguration {

  @Bean
  StaticSimulationStatisticsService simulationStatisticsService() {
    return new StaticSimulationStatisticsService();
  }
}
