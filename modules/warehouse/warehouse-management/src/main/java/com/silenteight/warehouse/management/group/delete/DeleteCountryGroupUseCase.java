package com.silenteight.warehouse.management.group.delete;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.management.country.update.UpdateCountriesUseCase;
import com.silenteight.warehouse.management.group.domain.CountryGroupService;

import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class DeleteCountryGroupUseCase {

  @NonNull
  private final CountryGroupService groupService;

  @NonNull
  private final UpdateCountriesUseCase updateCountriesUseCase;

  @Transactional
  void activate(@NonNull UUID id) {
    updateCountriesUseCase.activate(id, Collections.emptyList());
    groupService.delete(id);
    log.info("Country group and roles have been removed for countryGroupDtoId={}", id);
  }
}
