package com.silenteight.simulator.management.progress;

import com.silenteight.simulator.management.create.AnalysisService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class SimulationProgressConfiguration {

  @Bean
  SimulationProgressUseCase simulationProgressUseCase(
      SimulationProgressService simulationProgressService) {
    return new SimulationProgressUseCase(simulationProgressService);
  }

  @Bean
  SimulationProgressService simulationProgressService(
      AnalysisService analysisService,
      IndexedAlertProvider indexedAlertProvider,
      AnalysisNameQuery analysisNameQuery
  ) {
    return new SimulationProgressService(analysisService, indexedAlertProvider, analysisNameQuery);
  }
}
