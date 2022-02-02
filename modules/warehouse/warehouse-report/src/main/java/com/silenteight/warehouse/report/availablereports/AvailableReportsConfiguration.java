package com.silenteight.warehouse.report.availablereports;

import com.silenteight.warehouse.report.create.ReportPropertiesMatcher;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AvailableReportsConfiguration {

  @Bean
  ReportListService reportListService(
      ReportPropertiesMatcher reportPropertiesMatcher) {

    return new ReportListService(reportPropertiesMatcher);
  }
}
