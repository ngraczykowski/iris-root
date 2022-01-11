package com.silenteight.warehouse.report.billing.download;

import com.silenteight.sep.base.common.time.IsoUtcWithoutMillisDateFormatter;
import com.silenteight.warehouse.report.name.ReportFileName;
import com.silenteight.warehouse.report.storage.ReportStorage;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class DownloadBillingReportConfiguration {

  @Bean
  DownloadBillingReportUseCase downloadBillingReportUseCase(
      ReportDataQuery query,
      ReportStorage reportStorage,
      ReportFileName productionReportFileNameService) {

    return new DownloadBillingReportUseCase(
        query,
        reportStorage,
        productionReportFileNameService,
        IsoUtcWithoutMillisDateFormatter.INSTANCE);
  }
}
