package com.silenteight.warehouse.report.billing.domain;

import com.silenteight.warehouse.report.billing.generation.BillingReportGenerationService;
import com.silenteight.warehouse.report.reporting.ReportProperties;
import com.silenteight.warehouse.report.storage.ReportStorage;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.lang.Nullable;

import java.util.Objects;
import javax.validation.Valid;

@Configuration
@EntityScan
@EnableJpaRepositories
class BillingReportConfiguration {

  @Bean
  BillingReportService billingReportService(
      BillingReportRepository billingReportRepository,
      @Nullable BillingReportAsyncGenerationService asyncBillingReportGenerationService,
      ReportStorage reportStorage) {

    if (Objects.isNull(asyncBillingReportGenerationService)) {
      return null;
    } else {
      return new BillingReportService(
          billingReportRepository,
          asyncBillingReportGenerationService,
          reportStorage);
    }
  }

  @Bean
  BillingReportAsyncGenerationService asyncBillingReportGenerationService(
      BillingReportRepository billingReportRepository,
      @Nullable BillingReportGenerationService billingReportGenerationService,
      @Valid ReportProperties properties,
      ReportStorage reportStorage) {

    if (Objects.isNull(billingReportGenerationService)) {
      return null;
    } else {
      return new BillingReportAsyncGenerationService(
          billingReportRepository,
          billingReportGenerationService,
          properties.getBilling(),
          reportStorage);
    }
  }

  @Bean
  BillingReportQuery billingReportQuery(BillingReportRepository repository) {
    return new BillingReportQuery(repository);
  }
}
