package com.silenteight.sens.webapp.role.details;

import lombok.NonNull;

import com.silenteight.sens.webapp.role.details.dto.RoleDetailsDto;

import java.util.UUID;

public interface RoleDetailsQuery {

  RoleDetailsDto details(@NonNull UUID roleId);
}
