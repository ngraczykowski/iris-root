package com.silenteight.sens.webapp.permission.domain;

import com.silenteight.sens.webapp.permission.list.ListPermissionQuery;
import com.silenteight.sens.webapp.permission.list.dto.PermissionDto;

import java.util.Collection;
import java.util.UUID;

import static java.util.Collections.emptyList;

class PermissionQuery implements ListPermissionQuery {

  @Override
  public Collection<PermissionDto> listAll() {
    return emptyList();
  }

  @Override
  public Collection<PermissionDto> listPermissionsByRoleId(UUID roleId) {
    return emptyList();
  }
}
