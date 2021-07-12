package com.silenteight.warehouse.management.country.get;

import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.common.opendistro.roles.RoleService;
import com.silenteight.warehouse.management.group.get.GetCountryGroupQuery;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
class GetCountriesQuery {

  private final RoleService roleService;
  private final GetCountryGroupQuery getCountryGroupQuery;

  List<String> getCountries(UUID countryGroupId) {
    validateCountryGroupExists(countryGroupId);
    return roleService.getCountries(countryGroupId);
  }

  private void validateCountryGroupExists(UUID countryGroupId) {
    getCountryGroupQuery.get(countryGroupId);
  }
}
