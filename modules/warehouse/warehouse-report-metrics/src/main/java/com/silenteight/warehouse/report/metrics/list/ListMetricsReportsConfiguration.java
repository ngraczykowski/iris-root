package com.silenteight.warehouse.report.metrics.list;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ListMetricsReportsConfiguration {

  @Bean
  MetricsReportProvider metricsSimulationReportProvider() {
    return new MetricsReportProvider();
  }
}
