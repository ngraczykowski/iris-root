package com.silenteight.warehouse.report.billing.domain;

import com.silenteight.warehouse.report.billing.generation.BillingReportGenerationService;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan
@EnableJpaRepositories
class BillingReportConfiguration {

  @Bean
  @ConditionalOnBean(BillingReportGenerationService.class)
  BillingReportService billingReportService(
      BillingReportRepository billingReportRepository,
      BillingReportAsyncGenerationService asyncBillingReportGenerationService) {

    return new BillingReportService(billingReportRepository, asyncBillingReportGenerationService);
  }

  @Bean
  @ConditionalOnBean(BillingReportGenerationService.class)
  BillingReportAsyncGenerationService asyncBillingReportGenerationService(
      BillingReportRepository billingReportRepository,
      BillingReportGenerationService billingReportGenerationService) {

    return new BillingReportAsyncGenerationService(
        billingReportRepository, billingReportGenerationService);
  }

  @Bean
  @ConditionalOnBean(BillingReportGenerationService.class)
  BillingReportQuery billingReportQuery(BillingReportRepository repository) {
    return new BillingReportQuery(repository);
  }
}
