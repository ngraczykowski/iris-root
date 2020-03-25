package com.silenteight.sens.webapp.user.update.exception;

import lombok.NonNull;

public class RolesValidationException extends RuntimeException {

  private static final long serialVersionUID = -1440274680818692740L;

  private RolesValidationException(@NonNull String reason) {
    super(reason);
  }

  public static RolesValidationException of(@NonNull String reason) {
    return new RolesValidationException(reason);
  }
}
