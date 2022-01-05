package com.silenteight.warehouse.report.billing.domain;

import com.silenteight.warehouse.report.billing.generation.BillingReportGenerationService;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.lang.Nullable;

import java.util.Objects;

@Configuration
@EntityScan
@EnableJpaRepositories
class BillingReportConfiguration {

  @Bean
  BillingReportService billingReportService(
      BillingReportRepository billingReportRepository,
      @Nullable BillingReportAsyncGenerationService asyncBillingReportGenerationService) {
    if (Objects.isNull(asyncBillingReportGenerationService)) {
      return null;
    } else {
      return new BillingReportService(billingReportRepository, asyncBillingReportGenerationService);
    }
  }

  @Bean
  BillingReportAsyncGenerationService asyncBillingReportGenerationService(
      BillingReportRepository billingReportRepository,
      @Nullable BillingReportGenerationService billingReportGenerationService) {

    if (Objects.isNull(billingReportGenerationService)) {
      return null;
    } else {
      return new BillingReportAsyncGenerationService(
          billingReportRepository, billingReportGenerationService);
    }
  }

  @Bean
  BillingReportQuery billingReportQuery(BillingReportRepository repository) {
    return new BillingReportQuery(repository);
  }
}
