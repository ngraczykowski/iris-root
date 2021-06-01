package com.silenteight.warehouse.common.opendistro.tenant;

import com.silenteight.warehouse.common.opendistro.elastic.OpendistroElasticClient;
import com.silenteight.warehouse.common.opendistro.kibana.OpendistroKibanaClientFactory;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@Configuration
@EnableConfigurationProperties(TenantProperties.class)
class TenantConfiguration {

  @Bean
  TenantService tenantService(
      OpendistroKibanaClientFactory opendistroKibanaClientFactory,
      OpendistroElasticClient opendistroElasticClient,
      @Valid TenantProperties tenantProperties) {

    return new TenantService(
        opendistroKibanaClientFactory.getAdminClient(),
        opendistroElasticClient,
        tenantProperties.getMaxObjectCount());
  }
}
