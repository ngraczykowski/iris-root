package com.silenteight.warehouse.report.accuracy.v1.download;

import com.silenteight.warehouse.report.storage.ReportStorage;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class DeprecatedDownloadAccuracyReportConfiguration {

  @Bean
  DeprecatedDownloadAccuracyReportUseCase deprecatedDownloadAccuracyReportUseCase(
      DeprecatedAccuracyReportDataQuery query,
      ReportStorage reportStorage) {

    return new DeprecatedDownloadAccuracyReportUseCase(query, reportStorage);
  }
}
