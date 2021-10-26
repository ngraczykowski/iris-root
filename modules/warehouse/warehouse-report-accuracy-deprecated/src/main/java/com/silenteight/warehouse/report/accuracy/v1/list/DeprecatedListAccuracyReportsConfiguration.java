package com.silenteight.warehouse.report.accuracy.v1.list;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class DeprecatedListAccuracyReportsConfiguration {

  @Bean
  DeprecatedAccuracyReportProvider deprecatedAccuracyReportProvider() {
    return new DeprecatedAccuracyReportProvider();
  }
}
