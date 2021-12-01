package com.silenteight.simulator.management.progress;

import com.silenteight.simulator.management.create.AnalysisService;
import com.silenteight.simulator.management.domain.SimulationService;

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
      SimulationService simulationService
  ) {
    return new SimulationProgressService(analysisService, indexedAlertProvider, simulationService);
  }
}
