package com.silenteight.sep.usermanagement.api.role;

import lombok.NonNull;

import com.silenteight.sep.usermanagement.api.role.dto.CreateRoleCommand;

public interface RoleCreator {

  void create(@NonNull CreateRoleCommand command);
}
