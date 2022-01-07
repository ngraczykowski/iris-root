package com.silenteight.warehouse.report.billing.download;

import com.silenteight.sep.base.common.time.IsoUtcWithoutMillisDateFormatter;
import com.silenteight.warehouse.report.billing.domain.BillingReportService;
import com.silenteight.warehouse.report.name.ReportFileName;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
class DownloadBillingReportConfiguration {

  @Bean
  DownloadBillingReportUseCase downloadBillingReportUseCase(
      ReportDataQuery query,
      BillingReportService reportService,
      ReportFileName productionReportFileNameService) {

    if (Objects.isNull(reportService)) {
      return null;
    } else {
      return new DownloadBillingReportUseCase(
          query,
          reportService,
          productionReportFileNameService,
          IsoUtcWithoutMillisDateFormatter.INSTANCE);
    }
  }
}
