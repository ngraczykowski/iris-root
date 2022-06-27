package com.silenteight.simulator.management.create;

import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.simulator.dataset.domain.DatasetExternalResourceNameProvider;
import com.silenteight.simulator.dataset.domain.DatasetValidator;
import com.silenteight.simulator.management.domain.SimulationService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class CreateSimulationConfiguration {

  @Bean
  CreateSimulationUseCase createSimulationUseCase(
      ModelService modelService,
      AnalysisService analysisService,
      DatasetExternalResourceNameProvider datasetExternalResourceNameProvider,
      DatasetValidator datasetValidator,
      SimulationService simulationService,
      AuditingLogger auditingLogger) {

    return new CreateSimulationUseCase(
        modelService,
        analysisService,
        datasetExternalResourceNameProvider,
        datasetValidator,
        simulationService,
        auditingLogger);
  }
}
