package com.silenteight.warehouse.report.simulation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
class SimulationConfiguration {

  @Bean
  SimulationReportsUseCase simulationReportsUseCase(
      List<SimulationReportProvider> simulationReportProviders) {

    return new SimulationReportsUseCase(simulationReportProviders);
  }
}
