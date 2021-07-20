package com.silenteight.warehouse.report.sm.generation;

import com.silenteight.warehouse.indexer.indexing.IndexesQuery;
import com.silenteight.warehouse.indexer.query.grouping.GroupingQueryService;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(SimulationMetricsReportProperties.class)
class SimulationMetricsReportGenerationConfiguration {

  @Bean
  SimulationMetricsReportGenerationService simulationMetricsReportGenerationService(
      GroupingQueryService groupingQueryService,
      @Qualifier(value = "simulationIndexingQuery") IndexesQuery indexerQuery,
      SimulationMetricsReportProperties rbScorerProperties) {

    return new SimulationMetricsReportGenerationService(
        groupingQueryService, indexerQuery, rbScorerProperties);
  }
}
