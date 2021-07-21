package com.silenteight.warehouse.management.group.create;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.common.opendistro.roles.RolesMappingService;
import com.silenteight.warehouse.management.country.update.UpdateCountriesUseCase;
import com.silenteight.warehouse.management.group.domain.CountryGroupService;
import com.silenteight.warehouse.management.group.domain.dto.CountryGroupDto;

import static java.util.Collections.emptyList;

@Slf4j
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

    log.info("Country group created and roles has been added for countryGroupDtoId={}",
        countryGroupDto.getId());
  }
}
