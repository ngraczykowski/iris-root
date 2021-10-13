package com.silenteight.warehouse.report.metrics.create;

import com.silenteight.warehouse.report.metrics.domain.DeprecatedMetricsReportService;

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
