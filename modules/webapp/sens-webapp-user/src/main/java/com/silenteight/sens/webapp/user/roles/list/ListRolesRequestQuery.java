package com.silenteight.sens.webapp.user.roles.list;

import com.silenteight.sens.webapp.user.roles.list.dto.RoleDto;

import java.util.Collection;

public interface ListRolesRequestQuery {

  Collection<RoleDto> listAll();
}
