package com.silenteight.warehouse.report.rbs.download;

import com.silenteight.warehouse.report.rbs.domain.RbsReportService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class DownloadRbsReportConfiguration {

  @Bean
  DownloadRbsReportUseCase downloadRbsReportUseCase(
      RbsReportService reportService, RbsReportDataQuery query) {

    return new DownloadRbsReportUseCase(query, reportService);
  }
}
