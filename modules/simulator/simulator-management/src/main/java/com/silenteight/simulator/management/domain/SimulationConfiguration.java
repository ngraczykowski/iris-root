package com.silenteight.simulator.management.domain;

import com.silenteight.simulator.management.create.AnalysisService;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan
@EnableJpaRepositories
class SimulationConfiguration {

  @Bean
  SimulationService simulationService(SimulationEntityRepository simulationEntityRepository) {
    return new SimulationService(simulationEntityRepository);
  }

  @Bean
  SimulationQuery simulationQuery(
      SimulationEntityRepository simulationEntityRepository, AnalysisService analysisService) {

    return new SimulationQuery(simulationEntityRepository, analysisService);
  }
}
