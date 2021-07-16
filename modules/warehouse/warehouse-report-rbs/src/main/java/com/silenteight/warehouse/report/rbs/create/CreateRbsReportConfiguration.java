package com.silenteight.warehouse.report.rbs.create;

import com.silenteight.warehouse.report.rbs.domain.RbsReportService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class CreateRbsReportConfiguration {

  @Bean
  CreateRbsReportUseCase createReportUseCase(RbsReportService reportService) {
    return new CreateRbsReportUseCase(reportService);
  }
}
