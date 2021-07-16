package com.silenteight.warehouse.common.opendistro.roles;

import com.silenteight.warehouse.common.elastic.DlsQueryProcessor;
import com.silenteight.warehouse.common.opendistro.elastic.OpendistroElasticClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class RolesConfiguration {

  @Bean
  RoleService roleService(OpendistroElasticClient opendistroElasticClient) {
    return new RoleService(opendistroElasticClient, new DlsQueryProcessor());
  }

  @Bean
  RolesMappingService rolesMappingService(OpendistroElasticClient opendistroElasticClient) {
    return new RolesMappingService(opendistroElasticClient);
  }
}
