package com.silenteight.warehouse.report.rbs.generation;

import com.silenteight.warehouse.indexer.indexing.IndexesQuery;
import com.silenteight.warehouse.indexer.query.grouping.GroupingQueryService;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(RbsReportProperties.class)
class RbsReportGenerationConfiguration {

  @Bean
  RbsReportGenerationService rbsReportGenerationService(
      GroupingQueryService groupingQueryService,
      @Qualifier(value = "productionIndexingQuery") IndexesQuery indexerQuery,
      RbsReportProperties rbScorerProperties) {

    return new RbsReportGenerationService(groupingQueryService, indexerQuery, rbScorerProperties);
  }
}
