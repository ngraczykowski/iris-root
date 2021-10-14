package com.silenteight.warehouse.report.metrics.v1.generation;

import com.silenteight.warehouse.indexer.query.grouping.GroupingQueryService;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(MetricsReportProperties.class)
class DeprecatedMetricsReportGenerationConfiguration {

  @Bean
  DeprecatedMetricsReportGenerationService deprecatedMetricsReportGenerationService(
      GroupingQueryService groupingQueryService) {

    return new DeprecatedMetricsReportGenerationService(groupingQueryService);
  }
}
