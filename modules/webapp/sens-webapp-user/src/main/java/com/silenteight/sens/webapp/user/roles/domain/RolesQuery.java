package com.silenteight.sens.webapp.user.roles.domain;

import com.silenteight.sens.webapp.user.roles.details.RoleDetailsQuery;
import com.silenteight.sens.webapp.user.roles.list.ListRolesRequestQuery;
import com.silenteight.sens.webapp.user.roles.list.dto.RoleDto;

import java.util.Collection;
import java.util.UUID;

import static java.util.Collections.emptyList;

class RolesQuery implements RoleDetailsQuery, ListRolesRequestQuery {

  @Override
  public RoleDto details(UUID id) {
    return RoleDto.builder()
        .build();
  }

  @Override
  public Collection<RoleDto> listAll() {
    return emptyList();
  }
}
