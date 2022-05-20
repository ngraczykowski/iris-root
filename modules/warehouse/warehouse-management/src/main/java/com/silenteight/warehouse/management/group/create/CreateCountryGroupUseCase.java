package com.silenteight.warehouse.management.group.create;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.management.group.domain.CountryGroupService;
import com.silenteight.warehouse.management.group.domain.dto.CountryGroupDto;

@Slf4j
@RequiredArgsConstructor
class CreateCountryGroupUseCase {

  @NonNull
  private final CountryGroupService groupService;

  void activate(@NonNull CountryGroupDto countryGroupDto) {
    groupService.create(countryGroupDto);

    log.info("Country group created and roles has been added for countryGroupDtoId={}",
        countryGroupDto.getId());
  }
}
