package com.silenteight.warehouse.report.rbs.v1.create;

import com.silenteight.warehouse.report.rbs.v1.domain.DeprecatedRbsReportService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class DeprecatedCreateRbsReportConfiguration {

  @Bean
  DeprecatedCreateRbsReportUseCase deprecatedCreateReportUseCase(
      DeprecatedRbsReportService reportService) {
    return new DeprecatedCreateRbsReportUseCase(reportService);
  }
}
