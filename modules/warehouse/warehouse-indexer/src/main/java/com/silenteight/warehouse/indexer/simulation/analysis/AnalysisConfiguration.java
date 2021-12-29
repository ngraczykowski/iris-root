package com.silenteight.warehouse.indexer.simulation.analysis;

import com.silenteight.warehouse.common.environment.EnvironmentProperties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@Configuration
class AnalysisConfiguration {

  @Bean
  SimulationNamingStrategy simulationNamingStrategy(
      @Valid EnvironmentProperties environmentProperties) {
    return new SimulationNamingStrategy(environmentProperties.getPrefix());
  }
}

