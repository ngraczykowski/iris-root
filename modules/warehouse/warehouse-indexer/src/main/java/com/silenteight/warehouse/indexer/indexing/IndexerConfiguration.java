package com.silenteight.warehouse.indexer.indexing;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.common.environment.EnvironmentProperties;
import com.silenteight.warehouse.indexer.alert.AlertCopyDataService;
import com.silenteight.warehouse.indexer.alert.AlertIndexService;
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
      AlertIndexService alertIndexService,
      UniqueAnalysisFactory uniqueAnalysisFactory,
      AlertCopyDataService alertCopyDataService,
      TimeSource timeSource,
      @Valid EnvironmentProperties environmentProperties) {

    return new SimulationAlertIndexUseCase(
        alertIndexService,
        uniqueAnalysisFactory,
        alertCopyDataService,
        timeSource,
        environmentProperties.getPrefix());
  }

  @Bean
  ProductionAlertIndexUseCase productionAlertIndexUseCase(
      AlertIndexService alertIndexService,
      TimeSource timeSource,
      @Valid EnvironmentProperties environmentProperties) {

    return new ProductionAlertIndexUseCase(
        alertIndexService, timeSource, environmentProperties.getPrefix());
  }
}
