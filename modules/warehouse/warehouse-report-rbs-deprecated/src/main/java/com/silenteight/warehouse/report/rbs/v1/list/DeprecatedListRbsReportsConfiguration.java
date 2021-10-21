package com.silenteight.warehouse.report.rbs.v1.list;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeprecatedListRbsReportsConfiguration {

  @Bean
  DeprecatedRbsSimulationReportProvider deprecatedRbsSimulationReportProvider() {
    return new DeprecatedRbsSimulationReportProvider();
  }
}
