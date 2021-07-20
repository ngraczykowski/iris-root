package com.silenteight.simulator.management.domain;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan
@EnableJpaRepositories
class SimulationConfiguration {

  @Bean
  SimulationService simulationService(SimulationRepository repository) {
    return new SimulationService(repository);
  }

  @Bean
  SimulationQuery simulationQuery(SimulationRepository repository) {
    return new SimulationQuery(repository);
  }
}
