package com.silenteight.warehouse.management.country.update;

import com.silenteight.warehouse.common.opendistro.roles.RoleService;
import com.silenteight.warehouse.indexer.alert.ElasticsearchProperties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@Configuration
@EnableConfigurationProperties({ ElasticsearchProperties.class })
class UpdateCountriesConfiguration {

  @Bean
  UpdateCountriesUseCase updateCountriesUseCase(
      RoleService roleService,
      @Valid ElasticsearchProperties elasticsearchProperties) {

    return new UpdateCountriesUseCase(
        roleService, elasticsearchProperties.getProductionQueryIndex());
  }
}
