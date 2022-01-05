package com.silenteight.warehouse.report.rbs.generation;

import com.silenteight.warehouse.indexer.query.grouping.GroupingQueryService;
import com.silenteight.warehouse.report.reporting.ReportProperties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ReportProperties.class)
class RbsReportGenerationConfiguration {

  @Bean
  RbsReportGenerationService rbsReportGenerationService(GroupingQueryService groupingQueryService) {
    return new RbsReportGenerationService(groupingQueryService);
  }
}
