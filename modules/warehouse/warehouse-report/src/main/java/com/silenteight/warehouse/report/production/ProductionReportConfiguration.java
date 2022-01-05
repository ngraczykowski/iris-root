package com.silenteight.warehouse.report.production;

import com.silenteight.warehouse.report.reporting.ReportProperties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductionReportConfiguration {

  @Bean
  public ProductionReportsService productionReportsService(ReportProperties reportProperties) {
    return new ProductionReportsService(reportProperties);
  }

}
