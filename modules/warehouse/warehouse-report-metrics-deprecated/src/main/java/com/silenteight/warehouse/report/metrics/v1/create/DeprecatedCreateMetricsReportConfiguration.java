package com.silenteight.warehouse.report.metrics.v1.create;

import com.silenteight.warehouse.report.metrics.v1.domain.DeprecatedMetricsReportService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class DeprecatedCreateMetricsReportConfiguration {

  @Bean
  DeprecatedCreateMetricsReportUseCase deprecatedCreateMetricsReportUseCase(
      DeprecatedMetricsReportService service) {

    return new DeprecatedCreateMetricsReportUseCase(service);
  }
}
