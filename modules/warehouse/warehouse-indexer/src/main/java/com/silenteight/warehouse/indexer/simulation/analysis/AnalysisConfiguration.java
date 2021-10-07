package com.silenteight.warehouse.indexer.simulation.analysis;

import com.silenteight.warehouse.common.environment.EnvironmentProperties;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.validation.Valid;

@Configuration
@EntityScan
@EnableJpaRepositories
@EnableTransactionManagement
class AnalysisConfiguration {

  @Bean
  SimulationAnalysisService simulationAnalysisService(
      AnalysisMetadataRepository analysisMetadataRepository,
      ApplicationEventPublisher eventPublisher) {

    return new SimulationAnalysisService(analysisMetadataRepository, eventPublisher);
  }

  @Bean
  UniqueAnalysisFactory uniqueAnalysisFactory(
      SimulationAnalysisService simulationAnalysisService,
      @Valid EnvironmentProperties environmentProperties) {

    SimulationNamingStrategy namingStrategy =
        new SimulationNamingStrategy(environmentProperties.getPrefix());

    return new UniqueAnalysisFactory(simulationAnalysisService, namingStrategy);
  }
}
