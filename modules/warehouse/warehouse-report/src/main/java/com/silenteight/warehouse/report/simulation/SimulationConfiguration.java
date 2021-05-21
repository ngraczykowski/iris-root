package com.silenteight.warehouse.report.simulation;

import com.silenteight.warehouse.common.opendistro.tenant.TenantService;

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
}
