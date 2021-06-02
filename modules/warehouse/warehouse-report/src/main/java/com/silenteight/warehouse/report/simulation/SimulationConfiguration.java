package com.silenteight.warehouse.report.simulation;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.common.opendistro.elastic.OpendistroElasticClient;
import com.silenteight.warehouse.common.opendistro.kibana.OpendistroKibanaClientFactory;
import com.silenteight.warehouse.common.opendistro.tenant.TenantService;
import com.silenteight.warehouse.indexer.analysis.SimulationAnalysisService;
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
  SimulationService simulationService(
      ReportingService reportingService,
      SimulationReportingQuery simulationReportingQuery,
      TimeSource timeSource) {

    return new SimulationService(reportingService, simulationReportingQuery, timeSource);
  }

  @Bean
  SimulationReportingQuery simulationReportingQuery(
      ReportingService reportingService,
      SimulationAnalysisService simulationAnalysisService) {

    return new SimulationReportingQuery(reportingService, simulationAnalysisService);
  }

  @Bean
  UserAwareReportingService userAwareReportingService(
      OpendistroElasticClient opendistroElasticClient,
      OpendistroKibanaClientFactory opendistroKibanaClientFactory,
      SimulationAnalysisService simulationAnalysisService) {

    return new UserAwareReportingService(
        simulationAnalysisService,
        opendistroElasticClient,
        opendistroKibanaClientFactory.getUserAwareClient());
  }
}
