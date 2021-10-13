package com.silenteight.warehouse.report.metrics.list;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeprecatedListMetricsReportsConfiguration {

  @Bean
  DeprecatedMetricsReportProvider deprecatedMetricsReportProvider() {
    return new DeprecatedMetricsReportProvider();
  }
}
