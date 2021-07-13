package com.silenteight.warehouse.report.rbs.create;

import com.silenteight.warehouse.report.rbs.domain.RbsReportService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class CreateReportConfiguration {

  @Bean
  CreateReportUseCase createReportUseCase(RbsReportService reportService) {
    return new CreateReportUseCase(reportService);
  }
}
