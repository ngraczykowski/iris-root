package com.silenteight.simulator.management;

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
      SimulationEntityRepository simulationEntityRepository) {
    SimulationService simulationService = new SimulationService(simulationEntityRepository);
    return new CreateSimulationUseCase(simulationService);
  }

  @Bean
  SimulationQuery simulationQuery(
      SimulationEntityRepository simulationEntityRepository) {
    return new SimulationQuery(simulationEntityRepository);
  }
}
