package com.silenteight.warehouse.common.opendistro.tenant;

import com.silenteight.warehouse.common.opendistro.configuration.OpendistroConfiguration;
import com.silenteight.warehouse.common.opendistro.configuration.OpendistroProperties;
import com.silenteight.warehouse.common.opendistro.elastic.OpendistroElasticClient;
import com.silenteight.warehouse.common.opendistro.kibana.OpendistroKibanaClientFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.validation.Valid;

@Configuration
@Import(OpendistroConfiguration.class)
class TenantConfiguration {

  @Bean
  TenantService tenantService(
      OpendistroKibanaClientFactory opendistroKibanaClientFactory,
      OpendistroElasticClient opendistroElasticClient,
      @Valid OpendistroProperties opendistroProperties) {

    return new TenantService(
        opendistroKibanaClientFactory.getAdminClient(),
        opendistroElasticClient,
        opendistroProperties.getMaxObjectCount());
  }
}
