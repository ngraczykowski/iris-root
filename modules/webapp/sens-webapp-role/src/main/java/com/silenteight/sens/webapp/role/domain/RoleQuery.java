package com.silenteight.sens.webapp.role.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.role.details.RoleDetailsQuery;
import com.silenteight.sens.webapp.role.details.dto.RoleDetailsDto;
import com.silenteight.sens.webapp.role.list.ListRolesQuery;
import com.silenteight.sens.webapp.role.list.dto.RoleDto;

import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Transactional(readOnly = true)
class RoleQuery implements ListRolesQuery, RoleDetailsQuery {

  @NonNull
  private final RoleRepository repository;

  @Override
  public Collection<RoleDto> listAll() {
    return repository
        .findAll()
        .stream()
        .map(Role::toDto)
        .collect(toList());
  }

  @Override
  public RoleDetailsDto details(UUID id) {
    return RoleDetailsDto.builder()
        .build();
  }
}
