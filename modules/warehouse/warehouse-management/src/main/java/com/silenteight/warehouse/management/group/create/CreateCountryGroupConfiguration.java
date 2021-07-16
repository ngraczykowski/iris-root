package com.silenteight.warehouse.management.group.create;

import com.silenteight.warehouse.common.opendistro.roles.RolesMappingService;
import com.silenteight.warehouse.management.country.update.UpdateCountriesUseCase;
import com.silenteight.warehouse.management.group.domain.CountryGroupService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class CreateCountryGroupConfiguration {

  @Bean
  CreateCountryGroupUseCase createCountryGroupUseCase(
      CountryGroupService countryGroupService,
      UpdateCountriesUseCase updateCountriesUseCase,
      RolesMappingService rolesMappingService) {

    return new CreateCountryGroupUseCase(
        countryGroupService, updateCountriesUseCase, rolesMappingService);
  }
}
