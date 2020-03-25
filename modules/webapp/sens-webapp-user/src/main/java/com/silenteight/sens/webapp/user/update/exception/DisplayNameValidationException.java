package com.silenteight.sens.webapp.user.update.exception;

import lombok.NonNull;

public class DisplayNameValidationException extends RuntimeException {

  private static final long serialVersionUID = -2808863656277914625L;

  private DisplayNameValidationException(@NonNull String reason) {
    super(reason);
  }

  public static DisplayNameValidationException of(@NonNull String reason) {
    return new DisplayNameValidationException(reason);
  }
}
