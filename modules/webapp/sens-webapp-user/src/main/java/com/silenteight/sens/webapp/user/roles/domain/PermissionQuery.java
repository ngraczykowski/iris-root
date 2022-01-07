package com.silenteight.sens.webapp.user.roles.domain;

import com.silenteight.sens.webapp.user.roles.list.ListPermissionQuery;
import com.silenteight.sens.webapp.user.roles.list.dto.PermissionDto;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

class PermissionQuery implements ListPermissionQuery {

  @Override
  public Collection<PermissionDto> listAll() {
    return Collections.emptyList();
  }

  @Override
  public Collection<PermissionDto> listPermissionsByRoleId(UUID roleId) {
    return Collections.emptyList();
  }
}
