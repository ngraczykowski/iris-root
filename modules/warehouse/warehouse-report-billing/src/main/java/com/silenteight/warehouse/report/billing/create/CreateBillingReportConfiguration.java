package com.silenteight.warehouse.report.billing.create;

import com.silenteight.warehouse.indexer.query.IndexesQuery;
import com.silenteight.warehouse.report.billing.domain.BillingReportService;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class CreateBillingReportConfiguration {

  @Bean
  CreateBillingReportUseCase createBillingReportUseCase(
      BillingReportService reportService,
      @Qualifier(value = "productionIndexingQuery") IndexesQuery productionIndexerQuery) {

    return new CreateBillingReportUseCase(reportService, productionIndexerQuery);
  }
}
