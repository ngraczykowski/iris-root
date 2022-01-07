package com.silenteight.sens.webapp.user.roles.details;

import com.silenteight.sens.webapp.user.roles.list.dto.RoleDto;

import java.util.UUID;

public interface RoleDetailsQuery {

  RoleDto details(UUID id);
}
