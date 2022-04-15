package com.silenteight.sens.webapp.user.update.exception;

import lombok.NonNull;

public class DisplayNameUpdateException extends RuntimeException {

  private static final long serialVersionUID = 9214682747109743854L;

  private DisplayNameUpdateException(@NonNull String username, Exception e) {
    super("Could not update displayName of the user " + username, e);
  }

  public static DisplayNameUpdateException of(@NonNull String username, Exception e) {
    return new DisplayNameUpdateException(username, e);
  }
}
