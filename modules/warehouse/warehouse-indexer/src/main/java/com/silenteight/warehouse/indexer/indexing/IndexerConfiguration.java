package com.silenteight.warehouse.indexer.indexing;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.indexer.alert.AlertCopyDataService;
import com.silenteight.warehouse.indexer.alert.AlertIndexService;
import com.silenteight.warehouse.indexer.analysis.UniqueAnalysisFactory;
import com.silenteight.warehouse.indexer.indextracking.IndexByDiscriminatorResolverFactory;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(IndexerProperties.class)
class IndexerConfiguration {

  @Bean
  SimulationAlertIndexUseCase simulationAlertIndexUseCase(
      AlertIndexService alertIndexService,
      UniqueAnalysisFactory uniqueAnalysisFactory,
      AlertCopyDataService alertCopyDataService,
      TimeSource timeSource) {

    return new SimulationAlertIndexUseCase(
        alertIndexService,
        uniqueAnalysisFactory,
        alertCopyDataService,
        timeSource);
  }

  @Bean
  ProductionAlertIndexUseCase productionAlertIndexUseCase(
      AlertIndexService alertIndexService,
      TimeSource timeSource,
      IndexByDiscriminatorResolverFactory productionWriteIndexProviderFactory) {

    return new ProductionAlertIndexUseCase(
        alertIndexService, timeSource, productionWriteIndexProviderFactory);
  }
}
