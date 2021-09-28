package com.silenteight.warehouse.report.accuracy.download;

import com.silenteight.warehouse.report.storage.ReportStorage;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class DownloadAccuracyReportConfiguration {

  @Bean
  DownloadAccuracyReportUseCase downloadAccuracyReportUseCase(
      AccuracyReportDataQuery query,
      ReportStorage reportStorage) {

    return new DownloadAccuracyReportUseCase(query, reportStorage);
  }
}
