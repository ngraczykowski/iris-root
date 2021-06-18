package com.silenteight.warehouse.management.group.update;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.management.group.domain.CountryGroupService;
import com.silenteight.warehouse.management.group.update.dto.UpdateCountryGroupRequest;

import java.util.UUID;

@RequiredArgsConstructor
class UpdateCountryGroupUseCase {

  @NonNull
  private final CountryGroupService groupService;

  void activate(@NonNull UUID id, @NonNull UpdateCountryGroupRequest updateCountryGroupRequest) {
    groupService.update(id, updateCountryGroupRequest);
  }
}
