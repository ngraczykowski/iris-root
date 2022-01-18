package com.silenteight.sens.webapp.permission.list;

import com.silenteight.sens.webapp.permission.list.dto.PermissionDto;

import java.util.Collection;

public interface ListPermissionQuery {

  Collection<PermissionDto> listAll();
}
