package com.silenteight.sep.usermanagement.api.role;

import lombok.NonNull;

import com.silenteight.sep.usermanagement.api.role.dto.RolesDto;

public interface RolesQuery {

  RolesDto list(@NonNull String scope);
}
