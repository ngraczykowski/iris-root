package com.silenteight.warehouse.management.group.update;

import com.silenteight.warehouse.management.group.domain.CountryGroupService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class UpdateCountryGroupConfiguration {

  @Bean
  UpdateCountryGroupUseCase updateCountryGroupUseCase(CountryGroupService countryGroupService) {
    return new UpdateCountryGroupUseCase(countryGroupService);
  }
}
