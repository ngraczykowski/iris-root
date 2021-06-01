package com.silenteight.warehouse.report.reporting;

import com.silenteight.warehouse.common.opendistro.elastic.OpendistroElasticClient;
import com.silenteight.warehouse.common.opendistro.kibana.OpendistroKibanaClientFactory;
import com.silenteight.warehouse.indexer.analysis.AnalysisService;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan
@EnableJpaRepositories
class ReportingConfiguration {

  @Bean
  ReportingService reportingService(
      AnalysisService analysisService,
      OpendistroElasticClient opendistroElasticClient,
      OpendistroKibanaClientFactory opendistroKibanaClientFactory) {

    return new ReportingService(
        analysisService,
        opendistroElasticClient,
        opendistroKibanaClientFactory.getAdminClient());
  }

  @Bean
  UserAwareReportingService userAwareReportingService(
      OpendistroElasticClient opendistroElasticClient,
      OpendistroKibanaClientFactory opendistroKibanaClientFactory,
      AnalysisService analysisService) {

    return new UserAwareReportingService(
        analysisService,
        opendistroElasticClient,
        opendistroKibanaClientFactory.getUserAwareClient());
  }
}
