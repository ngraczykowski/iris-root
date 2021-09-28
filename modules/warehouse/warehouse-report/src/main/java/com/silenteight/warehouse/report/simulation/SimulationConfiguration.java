package com.silenteight.warehouse.report.simulation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
class SimulationConfiguration {

  @Bean
  SimulationReportsDefinitionsUseCase simulationReportsDefinitionsUseCase(
      List<SimulationReportsProvider> simulationReportsProviders) {

    return new SimulationReportsDefinitionsUseCase(simulationReportsProviders);
  }
}
