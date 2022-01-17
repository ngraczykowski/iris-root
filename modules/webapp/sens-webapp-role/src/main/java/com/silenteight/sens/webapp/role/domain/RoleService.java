package com.silenteight.sens.webapp.role.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.role.create.CreateRoleRequest;

@Slf4j
@RequiredArgsConstructor
public class RoleService {

  @NonNull
  private final RoleRepository repository;

  public void create(CreateRoleRequest request) {
    Role role = Role.builder()
        .roleId(request.getId())
        .name(request.getName())
        .description(request.getDescription())
        .permissionIds(request.getPermissions())
        .createdBy(request.getCreatedBy())
        .updatedBy(request.getCreatedBy())
        .build();

    repository.save(role);
    log.debug("Saved Role={}", role);
  }
}
