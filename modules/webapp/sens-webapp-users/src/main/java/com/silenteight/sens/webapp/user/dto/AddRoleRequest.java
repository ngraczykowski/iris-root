package com.silenteight.sens.webapp.user.dto;

import lombok.Data;
import lombok.NonNull;

import com.silenteight.sens.webapp.kernel.security.authority.Role;

@Data
public class AddRoleRequest {

  private final long userId;
  @NonNull
  private final Role role;
}
