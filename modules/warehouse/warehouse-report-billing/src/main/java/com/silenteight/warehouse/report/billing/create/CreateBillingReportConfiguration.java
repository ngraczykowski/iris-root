package com.silenteight.warehouse.report.billing.create;

import com.silenteight.warehouse.indexer.query.IndexesQuery;
import com.silenteight.warehouse.report.billing.domain.BillingReportService;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;

import java.util.Objects;

@Configuration
class CreateBillingReportConfiguration {

  @Bean
  CreateBillingReportUseCase createBillingReportUseCase(
      @Nullable BillingReportService reportService,
      @Qualifier(value = "productionIndexingQuery") IndexesQuery productionIndexerQuery) {

    if (Objects.isNull(reportService)) {
      return null;
    } else {
      return new CreateBillingReportUseCase(reportService, productionIndexerQuery);
    }
  }
}
