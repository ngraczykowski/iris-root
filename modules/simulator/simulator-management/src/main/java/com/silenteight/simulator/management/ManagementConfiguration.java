package com.silenteight.simulator.management;

import com.silenteight.auditing.bs.AuditingLogger;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan
@EnableJpaRepositories
class ManagementConfiguration {

  @Bean
  CreateSimulationUseCase createSimulationUseCase(
      ModelService modelService,
      AnalysisService analysisService,
      SimulationService simulationService,
      AuditingLogger auditingLogger) {

    return new CreateSimulationUseCase(
        modelService, analysisService, simulationService, auditingLogger);
  }

  @Bean
  SimulationService simulationService(SimulationEntityRepository simulationEntityRepository) {
    return new SimulationService(simulationEntityRepository);
  }

  @Bean
  SimulationQuery simulationQuery(SimulationEntityRepository simulationEntityRepository) {
    return new SimulationQuery(simulationEntityRepository);
  }
}
