package com.silenteight.warehouse.management.country.update;

import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.common.opendistro.roles.RoleService;

import java.util.Collection;
import java.util.UUID;
import javax.validation.Valid;

@RequiredArgsConstructor
public class UpdateCountriesUseCase {

  private final RoleService roleService;
  private final String productionQueryIndexPattern;

  public void activate(
      UUID countryGroupId, @Valid Collection<String> countries) {

    roleService.setCountries(countryGroupId, countries, productionQueryIndexPattern);
  }
}
