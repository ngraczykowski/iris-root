package com.silenteight.warehouse.report.metrics.create;

import com.silenteight.warehouse.report.metrics.domain.MetricsReportService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class CreateMetricsReportConfiguration {

  @Bean
  CreateMetricsReportUseCase createMetricsReportUseCase(MetricsReportService service) {
    return new CreateMetricsReportUseCase(service);
  }
}
