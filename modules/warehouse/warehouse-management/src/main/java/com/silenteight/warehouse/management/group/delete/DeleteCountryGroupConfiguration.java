package com.silenteight.warehouse.management.group.delete;

import com.silenteight.warehouse.management.country.update.UpdateCountriesUseCase;
import com.silenteight.warehouse.management.group.domain.CountryGroupService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeleteCountryGroupConfiguration {

  @Bean
  DeleteCountryGroupUseCase deleteCountryGroupUseCase(
      CountryGroupService groupService, UpdateCountriesUseCase updateCountriesUseCase) {

    return new DeleteCountryGroupUseCase(groupService, updateCountriesUseCase);
  }
}
