package com.silenteight.warehouse.report.billing.download;

import com.silenteight.sep.base.common.time.IsoUtcWithoutMillisDateFormatter;
import com.silenteight.warehouse.report.billing.domain.BillingReportService;
import com.silenteight.warehouse.report.billing.generation.BillingReportGenerationService;
import com.silenteight.warehouse.report.name.ReportFileName;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class DownloadBillingReportConfiguration {

  @Bean
  @ConditionalOnBean(BillingReportGenerationService.class)
  DownloadBillingReportUseCase downloadBillingReportUseCase(
      ReportDataQuery query,
      BillingReportService reportService,
      ReportFileName productionReportFileNameService) {

    return new DownloadBillingReportUseCase(
        query,
        reportService,
        productionReportFileNameService,
        IsoUtcWithoutMillisDateFormatter.INSTANCE);
  }
}
