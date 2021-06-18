package com.silenteight.warehouse.management.group.create;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.management.group.domain.CountryGroupService;
import com.silenteight.warehouse.management.group.domain.dto.CountryGroupDto;

@RequiredArgsConstructor
class CreateCountryGroupUseCase {

  @NonNull
  private final CountryGroupService groupService;

  void activate(@NonNull CountryGroupDto countryGroupDto) {
    groupService.create(countryGroupDto);
  }
}
