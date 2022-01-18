package com.silenteight.sens.webapp.permission.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.permission.list.ListPermissionQuery;
import com.silenteight.sens.webapp.permission.list.dto.PermissionDto;

import java.util.Collection;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
class PermissionQuery implements ListPermissionQuery {

  @NonNull
  private final PermissionRepository repository;

  @Override
  public Collection<PermissionDto> listAll() {
    return repository
        .findAll()
        .stream()
        .map(Permission::toDto)
        .collect(toList());
  }
}
