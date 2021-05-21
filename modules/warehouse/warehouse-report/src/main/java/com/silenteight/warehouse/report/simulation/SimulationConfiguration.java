package com.silenteight.warehouse.report.simulation;

import com.silenteight.warehouse.common.opendistro.kibana.OpendistroKibanaClient;
import com.silenteight.warehouse.common.opendistro.tenant.TenantService;
import com.silenteight.warehouse.indexer.analysis.AnalysisService;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@Configuration
@EnableConfigurationProperties(SimulationProperties.class)
class SimulationConfiguration {

  @Bean
  GenerateSimulationReportsUseCase generateSimulationReportsUseCase(
      AnalysisService analysisService, OpendistroKibanaClient opendistroKibanaClient,
      TenantService tenantService, @Valid SimulationProperties simulationProperties) {

    return new GenerateSimulationReportsUseCase(analysisService,
        opendistroKibanaClient, tenantService,
        simulationProperties.getMasterTenant(),
        simulationProperties.getPollingIntervalMs(),
        simulationProperties.getPollingMaxAttemptCount());
  }
}
