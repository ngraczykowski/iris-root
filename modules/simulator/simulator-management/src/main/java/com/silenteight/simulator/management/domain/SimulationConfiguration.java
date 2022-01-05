package com.silenteight.simulator.management.domain;


import com.silenteight.sep.base.common.time.TimeSource;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@Configuration
@EntityScan
@EnableJpaRepositories
class SimulationConfiguration {

  @Bean
  SimulationService simulationService(SimulationRepository repository, TimeSource timeSource) {
    return new SimulationService(repository, timeSource);
  }

  @Bean
  SimulationQuery simulationQuery(SimulationRepository repository) {
    return new SimulationQuery(repository);
  }
}
