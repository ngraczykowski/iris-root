package com.silenteight.sens.webapp.user.update.exception;

import lombok.NonNull;

public class RolesUpdateException extends RuntimeException {

  private static final long serialVersionUID = 9214682747109743854L;

  private RolesUpdateException(@NonNull String username, Throwable e) {
    super("Could not update roles of the user " + username, e);
  }

  public static RolesUpdateException of(@NonNull String username, Throwable e) {
    return new RolesUpdateException(username, e);
  }
}
