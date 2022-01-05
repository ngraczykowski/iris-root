package com.silenteight.warehouse.report.metrics.generation;

import com.silenteight.warehouse.indexer.query.grouping.GroupingQueryService;
import com.silenteight.warehouse.report.reporting.ReportProperties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ReportProperties.class)
class MetricsReportGenerationConfiguration {

  @Bean
  MetricsReportGenerationService simulationMetricsReportGenerationService(
      GroupingQueryService groupingQueryService) {

    return new MetricsReportGenerationService(groupingQueryService);
  }
}
