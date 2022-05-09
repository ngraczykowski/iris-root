package com.silenteight.sep.usermanagement.api.role;

import lombok.NonNull;

public class RoleValidationException extends RuntimeException {

  private static final long serialVersionUID = -1440274680818692740L;

  private RoleValidationException(@NonNull String reason) {
    super(reason);
  }

  public static RoleValidationException of(@NonNull String reason) {
    return new RoleValidationException(reason);
  }
}
