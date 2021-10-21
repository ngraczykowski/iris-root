package com.silenteight.warehouse.report.rbs.v1.download;

import com.silenteight.warehouse.report.rbs.v1.domain.DeprecatedRbsReportService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class DeprecatedDownloadRbsReportConfiguration {

  @Bean
  DeprecatedDownloadRbsReportUseCase deprecatedDownloadRbsReportUseCase(
      DeprecatedRbsReportService reportService, DeprecatedRbsReportDataQuery query) {

    return new DeprecatedDownloadRbsReportUseCase(query, reportService);
  }
}
