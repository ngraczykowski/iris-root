package com.silenteight.warehouse.report.tenant;

import com.silenteight.warehouse.common.opendistro.kibana.OpendistroKibanaClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class TenantConfiguration {

  @Bean
  TenantService tenantService(OpendistroKibanaClient opendistroKibanaClient) {
    return new TenantService(opendistroKibanaClient);
  }
}
