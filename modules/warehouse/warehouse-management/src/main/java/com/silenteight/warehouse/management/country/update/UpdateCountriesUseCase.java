package com.silenteight.warehouse.management.country.update;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.common.domain.country.CountryPermissionService;

import java.util.Collection;
import java.util.UUID;
import javax.validation.Valid;

@RequiredArgsConstructor
public class UpdateCountriesUseCase {

  @NonNull
  private final CountryPermissionService countryPermissionService;

  public void activate(UUID countryGroupId, @Valid Collection<String> countries) {
    countryPermissionService.setCountries(countryGroupId, countries);
  }
}
