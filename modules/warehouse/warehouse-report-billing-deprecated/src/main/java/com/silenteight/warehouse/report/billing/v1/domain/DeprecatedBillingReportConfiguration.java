package com.silenteight.warehouse.report.billing.v1.domain;

import com.silenteight.warehouse.report.billing.v1.generation.DeprecatedBillingReportGenerationService;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan
@EnableJpaRepositories
class DeprecatedBillingReportConfiguration {

  @Bean
  DeprecatedBillingReportService deprecatedBillingReportService(
      DeprecatedBillingReportRepository billingReportRepository,
      DeprecatedBillingReportAsyncGenerationService asyncBillingReportGenerationService) {

    return new DeprecatedBillingReportService(
        billingReportRepository, asyncBillingReportGenerationService);
  }

  @Bean
  DeprecatedBillingReportAsyncGenerationService deprecatedBillingReportAsyncGenerationService(
      DeprecatedBillingReportRepository billingReportRepository,
      DeprecatedBillingReportGenerationService billingReportGenerationService) {

    return new DeprecatedBillingReportAsyncGenerationService(
        billingReportRepository, billingReportGenerationService);
  }

  @Bean
  DeprecatedBillingReportQuery deprecatedBillingReportQuery(
      DeprecatedBillingReportRepository repository) {

    return new DeprecatedBillingReportQuery(repository);
  }
}
