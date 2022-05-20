package com.silenteight.warehouse.management.country.get;

import com.silenteight.warehouse.common.domain.country.CountryPermissionService;
import com.silenteight.warehouse.management.group.get.GetCountryGroupQuery;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class GetCountriesConfiguration {

  @Bean
  GetCountriesQuery getCountriesQuery(
      CountryPermissionService countryPermissionService,
      GetCountryGroupQuery getCountryGroupQuery) {

    return new GetCountriesQuery(countryPermissionService, getCountryGroupQuery);
  }
}
