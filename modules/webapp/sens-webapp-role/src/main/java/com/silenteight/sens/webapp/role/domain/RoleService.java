package com.silenteight.sens.webapp.role.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.role.create.CreateRoleRequest;
import com.silenteight.sens.webapp.role.domain.exception.RoleAlreadyAssignedToUserException;
import com.silenteight.sens.webapp.role.domain.exception.RoleNotFoundException;
import com.silenteight.sens.webapp.role.validate.RoleAssignmentValidator;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class RoleService {

  @NonNull
  private final RoleRepository repository;
  @NonNull
  private final RoleAssignmentValidator roleAssignmentValidator;

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
    log.info("Saved Role={}", role);
  }

  public void remove(@NonNull UUID roleId) {
    Role role = repository
        .findByRoleId(roleId)
        .orElseThrow(() -> new RoleNotFoundException(roleId));

    if (roleAssignmentValidator.isAssigned(role.getName()))
      throw new RoleAlreadyAssignedToUserException(role.getName());

    repository.delete(role);
    log.info("Role removed. roleId={}", roleId);
  }
}
