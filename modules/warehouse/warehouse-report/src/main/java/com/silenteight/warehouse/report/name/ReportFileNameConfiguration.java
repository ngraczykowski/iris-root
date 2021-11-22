package com.silenteight.warehouse.report.name;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@Configuration
@EnableConfigurationProperties(ReportFileNameProperties.class)
class ReportFileNameConfiguration {

  @Bean
  ProductionReportFileNameService productionReportFileNameService(
      @Valid ReportFileNameProperties fileNameProperties) {

    return new ProductionReportFileNameService(fileNameProperties.getProductionPattern());
  }

  @Bean
  SimulationReportFileNameService simulationReportFileNameService(
      @Valid ReportFileNameProperties fileNameProperties) {

    return new SimulationReportFileNameService(fileNameProperties.getSimulationPattern());
  }
}
