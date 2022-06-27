package com.silenteight.simulator.management.archive;

import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.simulator.management.domain.SimulationService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ArchiveSimulationConfiguration {

  @Bean
  ArchiveSimulationUseCase archiveSimulationUseCase(
      SimulationService simulationService, AuditingLogger auditingLogger) {

    return new ArchiveSimulationUseCase(simulationService, auditingLogger);
  }
}
