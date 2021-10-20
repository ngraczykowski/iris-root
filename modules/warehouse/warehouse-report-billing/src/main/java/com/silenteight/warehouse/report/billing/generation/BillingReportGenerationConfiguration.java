package com.silenteight.warehouse.report.billing.generation;

import com.silenteight.warehouse.indexer.query.grouping.GroupingQueryService;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@Configuration
@EnableConfigurationProperties(BillingReportProperties.class)
class BillingReportGenerationConfiguration {

  @Bean
  BillingReportGenerationService billingReportGenerationService(
      GroupingQueryService groupingQueryService,
      @Valid BillingReportProperties rbScorerProperties) {

    return new BillingReportGenerationService(groupingQueryService, rbScorerProperties);
  }
}
