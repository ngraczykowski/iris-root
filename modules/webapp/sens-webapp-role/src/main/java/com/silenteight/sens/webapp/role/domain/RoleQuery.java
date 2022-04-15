package com.silenteight.sens.webapp.role.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.role.details.RoleDetailsQuery;
import com.silenteight.sens.webapp.role.details.dto.RoleDetailsDto;
import com.silenteight.sens.webapp.role.domain.exception.RoleNotFoundException;
import com.silenteight.sens.webapp.role.list.ListRolesQuery;
import com.silenteight.sens.webapp.role.list.dto.RoleDto;

import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
class RoleQuery implements ListRolesQuery, RoleDetailsQuery {

  @NonNull
  private final RoleRepository repository;

  @Override
  public Collection<RoleDto> listAll() {
    log.debug("Listing all RoleDto");
    return repository
        .findAll()
        .stream()
        .map(Role::toDto)
        .collect(toList());
  }

  @Override
  public RoleDetailsDto details(UUID roleId) {
    log.debug("Getting RoleDetailsDto by roleId={}", roleId);
    return repository
        .findByRoleId(roleId)
        .map(Role::toDetailsDto)
        .orElseThrow(() -> new RoleNotFoundException(roleId));
  }
}
