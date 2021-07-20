package com.silenteight.warehouse.report.simulation;

import com.silenteight.warehouse.common.opendistro.tenant.TenantService;
import com.silenteight.warehouse.indexer.analysis.SimulationAnalysisService;
import com.silenteight.warehouse.report.reporting.ReportingService;
import com.silenteight.warehouse.report.reporting.UserAwareReportingService;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import javax.validation.Valid;

@Configuration
@EnableConfigurationProperties(SimulationProperties.class)
class SimulationConfiguration {

  @Bean
  KibanaSetupForSimulationUseCase kibanaSetupForSimulationUseCase(
      TenantService tenantService,
      @Valid SimulationProperties simulationProperties) {

    return new KibanaSetupForSimulationUseCase(
        tenantService,
        simulationProperties.getMasterTenant());
  }

  @Bean
  SimulationService simulationService(
      ReportingService reportingService,
      SimulationReportingQuery simulationReportingQuery,
      UserAwareReportingService userAwareReportingService) {

    return new SimulationService(
        reportingService, simulationReportingQuery, userAwareReportingService);
  }

  @Bean
  SimulationReportingQuery simulationReportingQuery(
      ReportingService reportingService, SimulationAnalysisService simulationAnalysisService) {

    return new SimulationReportingQuery(reportingService, simulationAnalysisService);
  }

  @Bean
  SimulationReportsDefinitionsUseCase simulationReportsDefinitionsUseCase(
      SimulationReportingQuery simulationReportingQuery,
      List<SimulationReportsProvider> simulationReportsProviders) {

    return new SimulationReportsDefinitionsUseCase(
        simulationReportingQuery, simulationReportsProviders);
  }
}
