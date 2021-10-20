package com.silenteight.warehouse.report.billing.v1.generation;

import com.silenteight.warehouse.indexer.query.IndexesQuery;
import com.silenteight.warehouse.indexer.query.grouping.GroupingQueryService;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(BillingReportProperties.class)
class DeprecatedBillingReportGenerationConfiguration {

  @Bean
  DeprecatedBillingReportGenerationService deprecatedBillingReportGenerationService(
      GroupingQueryService groupingQueryService,
      @Qualifier(value = "productionIndexingQuery") IndexesQuery indexerQuery,
      BillingReportProperties rbScorerProperties) {

    return new DeprecatedBillingReportGenerationService(
        groupingQueryService, indexerQuery, rbScorerProperties);
  }
}
