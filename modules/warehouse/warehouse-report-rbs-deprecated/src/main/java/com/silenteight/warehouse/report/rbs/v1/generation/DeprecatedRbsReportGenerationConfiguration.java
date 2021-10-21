package com.silenteight.warehouse.report.rbs.v1.generation;

import com.silenteight.warehouse.indexer.query.grouping.GroupingQueryService;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(RbsReportProperties.class)
class DeprecatedRbsReportGenerationConfiguration {

  @Bean
  DeprecatedRbsReportGenerationService deprecatedRbsReportGenerationService(
      GroupingQueryService groupingQueryService) {

    return new DeprecatedRbsReportGenerationService(groupingQueryService);
  }
}
