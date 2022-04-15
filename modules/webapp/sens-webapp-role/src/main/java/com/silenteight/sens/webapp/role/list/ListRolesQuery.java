package com.silenteight.sens.webapp.role.list;

import com.silenteight.sens.webapp.role.list.dto.RoleDto;

import java.util.Collection;

public interface ListRolesQuery {

  Collection<RoleDto> listAll();
}
