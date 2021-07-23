package com.silenteight.warehouse.management.country.update;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.common.opendistro.roles.RoleService;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;

@RequiredArgsConstructor
public class UpdateCountriesUseCase {

  @NonNull
  private final RoleService roleService;
  @NonNull
  private final List<String> countryRolesIndexes;

  public void activate(
      UUID countryGroupId, @Valid Collection<String> countries) {

    roleService.setCountries(countryGroupId, countries, countryRolesIndexes);
  }
}
