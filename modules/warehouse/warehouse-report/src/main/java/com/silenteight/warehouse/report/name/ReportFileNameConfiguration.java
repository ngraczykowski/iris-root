package com.silenteight.warehouse.report.name;

import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.common.domain.ReportConstants;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import javax.validation.Valid;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(ReportFileNameProperties.class)
class ReportFileNameConfiguration {

  @Valid
  private final ReportFileNameProperties fileNameProperties;

  @Bean
  ProductionReportFileNameService productionReportFileNameService() {
    return new ProductionReportFileNameService(fileNameProperties.getProductionPattern());
  }

  @Bean
  SimulationReportFileNameService simulationReportFileNameService() {
    return new SimulationReportFileNameService(fileNameProperties.getSimulationPattern());
  }

  @Bean
  Map<String, ReportFileName> reportNameResolvers() {
    return Map.of(
        ReportConstants.PRODUCTION, productionReportFileNameService(),
        ReportConstants.SIMULATION, simulationReportFileNameService()
    );
  }
}
