package com.silenteight.warehouse.report.reporting;

import com.silenteight.warehouse.common.opendistro.elastic.OpendistroElasticClient;
import com.silenteight.warehouse.common.opendistro.kibana.OpendistroKibanaClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ReportingConfiguration {

  @Bean
  ReportingService reportingService(
      OpendistroElasticClient opendistroElasticClient,
      OpendistroKibanaClient opendistroKibanaClient) {

    return new ReportingService(opendistroElasticClient, opendistroKibanaClient);
  }
}
