package com.silenteight.warehouse.report.simulation;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.common.opendistro.tenant.TenantService;
import com.silenteight.warehouse.report.reporting.ReportingService;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
  SimulationService simulationService(ReportingService reportingService, TimeSource timeSource) {
    return new SimulationService(reportingService, timeSource);
  }

  @Bean
  SimulationReportingQuery simulationReportingQuery(ReportingService reportingService) {
    return new SimulationReportingQuery(reportingService);
  }
}
