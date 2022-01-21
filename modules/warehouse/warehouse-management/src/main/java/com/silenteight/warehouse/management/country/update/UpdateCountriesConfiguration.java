package com.silenteight.warehouse.management.country.update;

import com.silenteight.warehouse.common.domain.country.CountryPermissionService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
class UpdateCountriesConfiguration {

  @Bean
  UpdateCountriesUseCase updateCountriesUseCase(CountryPermissionService countryPermissionService) {
    return new UpdateCountriesUseCase(countryPermissionService);
  }
}
