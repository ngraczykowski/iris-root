package com.silenteight.warehouse.retention.simulation;

import com.silenteight.warehouse.indexer.alert.indexing.AlertIndexService;
import com.silenteight.warehouse.indexer.alert.indexing.ElasticsearchProperties;
import com.silenteight.warehouse.indexer.query.streaming.AllDataProvider;
import com.silenteight.warehouse.indexer.simulation.analysis.SimulationNamingStrategy;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@Configuration
@EnableConfigurationProperties(RetentionSimulationProperties.class)
class RetentionSimulationConfiguration {

  @Bean
  RetentionSimulationUseCase simulationRetentionUseCase(
      AlertIndexService alertIndexService,
      SimulationNamingStrategy simulationNamingStrategy,
      AllDocumentsInIndexProcessor allDocumentsInIndexProcessor,
      @Valid RetentionSimulationProperties retentionSimulationProperties,
      @Valid ElasticsearchProperties elasticsearchProperties) {

    return new RetentionSimulationUseCase(
        alertIndexService,
        simulationNamingStrategy,
        allDocumentsInIndexProcessor,
        retentionSimulationProperties,
        elasticsearchProperties.getUpdateRequestBatchSize());
  }

  @Bean
  AllDocumentsInIndexProcessor allDocumentsInIndexProcessor(AllDataProvider dataProvider) {
    return new AllDocumentsInIndexProcessor(dataProvider);
  }
}
