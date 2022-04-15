package com.silenteight.simulator.management.create;

import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.simulator.dataset.domain.DatasetQuery;
import com.silenteight.simulator.management.domain.SimulationService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class CreateSimulationConfiguration {

  @Bean
  CreateSimulationUseCase createSimulationUseCase(
      ModelService modelService,
      AnalysisService analysisService,
      DatasetQuery datasetQuery,
      SimulationService simulationService,
      AuditingLogger auditingLogger) {

    return new CreateSimulationUseCase(
        modelService, analysisService, datasetQuery, simulationService, auditingLogger);
  }
}
