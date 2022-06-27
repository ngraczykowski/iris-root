package com.silenteight.simulator.management.cancel;

import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.simulator.management.domain.SimulationService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class CancelSimulationConfiguration {

  @Bean
  CancelSimulationUseCase cancelSimulationUseCase(
      SimulationService simulationService, AuditingLogger auditingLogger) {

    return new CancelSimulationUseCase(simulationService, auditingLogger);
  }
}
