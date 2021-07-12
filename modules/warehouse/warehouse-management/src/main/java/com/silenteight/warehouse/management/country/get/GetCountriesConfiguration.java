package com.silenteight.warehouse.management.country.get;

import com.silenteight.warehouse.common.opendistro.roles.RoleService;
import com.silenteight.warehouse.management.group.get.GetCountryGroupQuery;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class GetCountriesConfiguration {

  @Bean
  GetCountriesQuery getCountriesQuery(
      RoleService roleService, GetCountryGroupQuery getCountryGroupQuery) {

    return new GetCountriesQuery(roleService, getCountryGroupQuery);
  }
}
