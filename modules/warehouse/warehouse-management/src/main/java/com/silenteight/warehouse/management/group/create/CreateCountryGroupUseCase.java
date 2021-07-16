package com.silenteight.warehouse.management.group.create;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.common.opendistro.roles.RolesMappingService;
import com.silenteight.warehouse.management.country.update.UpdateCountriesUseCase;
import com.silenteight.warehouse.management.group.domain.CountryGroupService;
import com.silenteight.warehouse.management.group.domain.dto.CountryGroupDto;

import static java.util.Collections.emptyList;

@RequiredArgsConstructor
class CreateCountryGroupUseCase {

  @NonNull
  private final CountryGroupService groupService;

  @NonNull
  private final UpdateCountriesUseCase updateCountriesUseCase;

  @NonNull
  RolesMappingService rolesMappingService;

  void activate(@NonNull CountryGroupDto countryGroupDto) {
    groupService.create(countryGroupDto);
    updateCountriesUseCase.activate(countryGroupDto.getId(), emptyList());
    rolesMappingService.attachBackendRoleToRole(countryGroupDto.getId());
  }
}
