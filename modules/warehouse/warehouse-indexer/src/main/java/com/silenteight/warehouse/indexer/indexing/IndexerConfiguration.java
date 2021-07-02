package com.silenteight.warehouse.indexer.indexing;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.common.environment.EnvironmentProperties;
import com.silenteight.warehouse.indexer.alert.AlertService;
import com.silenteight.warehouse.indexer.analysis.UniqueAnalysisFactory;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@Configuration
@EnableConfigurationProperties({
    IndexerIntegrationProperties.class
})
class IndexerConfiguration {

  @Bean
  SimulationAlertIndexUseCase simulationAlertIndexUseCase(
      AlertService alertService,
      UniqueAnalysisFactory uniqueAnalysisFactory,
      TimeSource timeSource,
      @Valid EnvironmentProperties environmentProperties) {

    return new SimulationAlertIndexUseCase(
        alertService, uniqueAnalysisFactory, timeSource, environmentProperties.getPrefix());
  }

  @Bean
  ProductionAlertIndexUseCase productionAlertIndexUseCase(
      AlertService alertService,
      TimeSource timeSource,
      @Valid EnvironmentProperties environmentProperties) {

    return new ProductionAlertIndexUseCase(
        alertService, timeSource, environmentProperties.getPrefix());
  }
}
