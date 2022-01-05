package com.silenteight.warehouse.report.billing.generation;

import com.silenteight.warehouse.indexer.query.grouping.GroupingQueryService;
import com.silenteight.warehouse.report.reporting.ReportProperties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;
import javax.validation.Valid;

@Configuration
@EnableConfigurationProperties(ReportProperties.class)
class BillingReportGenerationConfiguration {

  @Bean
  BillingReportGenerationService billingReportGenerationService(
      GroupingQueryService groupingQueryService,
      @Valid ReportProperties reportProperties) {

    if (Objects.isNull(reportProperties.getBilling())) {
      return null;
    } else {
      return new BillingReportGenerationService(groupingQueryService,
          reportProperties.getBilling());
    }
  }
}
