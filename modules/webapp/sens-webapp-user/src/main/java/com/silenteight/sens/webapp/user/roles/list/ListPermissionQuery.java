package com.silenteight.sens.webapp.user.roles.list;

import com.silenteight.sens.webapp.user.roles.list.dto.PermissionDto;

import java.util.Collection;
import java.util.UUID;

public interface ListPermissionQuery {

  Collection<PermissionDto> listAll();

  Collection<PermissionDto> listPermissionsByRoleId(UUID roleId);
}
