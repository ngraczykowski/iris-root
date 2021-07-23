package com.silenteight.warehouse.indexer.indexing;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.common.environment.EnvironmentProperties;
import com.silenteight.warehouse.indexer.alert.AlertCopyDataService;
import com.silenteight.warehouse.indexer.alert.AlertService;
import com.silenteight.warehouse.indexer.analysis.UniqueAnalysisFactory;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@Configuration
@EnableConfigurationProperties(IndexerIntegrationProperties.class)
class IndexerConfiguration {

  @Bean
  SimulationAlertIndexUseCase simulationAlertIndexUseCase(
      AlertService alertService,
      UniqueAnalysisFactory uniqueAnalysisFactory,
      AlertCopyDataService alertCopyDataService,
      TimeSource timeSource,
      @Valid EnvironmentProperties environmentProperties) {

    return new SimulationAlertIndexUseCase(
        alertService,
        uniqueAnalysisFactory,
        alertCopyDataService,
        timeSource,
        environmentProperties.getPrefix());
  }

  @Bean
  ProductionAlertIndexUseCase productionAlertIndexUseCase(
      AlertService alertService,
      TimeSource timeSource,
      @Valid EnvironmentProperties environmentProperties) {

    return new ProductionAlertIndexUseCase(
        alertService, timeSource, environmentProperties.getPrefix());
  }

  @Bean
  ProductionIndexingQuery productionIndexingQuery(
      @Valid EnvironmentProperties environmentProperties) {
    return new ProductionIndexingQuery(environmentProperties.getPrefix());
  }

  @Bean
  SimulationIndexingQuery simulationIndexingQuery(
      @Valid EnvironmentProperties environmentProperties) {
    return new SimulationIndexingQuery(environmentProperties.getPrefix());
  }
}
