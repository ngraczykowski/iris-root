package com.silenteight.warehouse.management.group.delete;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.common.opendistro.roles.RoleService;
import com.silenteight.warehouse.common.opendistro.roles.RolesMappingService;
import com.silenteight.warehouse.management.group.domain.CountryGroupService;

import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class DeleteCountryGroupUseCase {

  @NonNull
  private final CountryGroupService groupService;

  @NonNull
  private final RolesMappingService rolesMappingService;

  @NonNull
  private final RoleService roleService;

  @Transactional
  void activate(@NonNull UUID id) {
    groupService.delete(id);
    rolesMappingService.removeRoleMapping(id);
    roleService.removeRole(id);

    log.info("Country group and roles have been removed for countryGroupDtoId={}", id);
  }
}
