package com.silenteight.warehouse.report.rbs.list;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ListRbsReportsConfiguration {

  @Bean
  RbsSimulationReportProvider rbsSimulationReportProvider() {
    return new RbsSimulationReportProvider();
  }
}
