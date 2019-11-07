package com.silenteight.sens.webapp.user.exception;

public class UserNotFoundException extends RuntimeException {

  private static final long serialVersionUID = -7464248469197950082L;

  public UserNotFoundException(String userName) {
    super(String.format("User with name '%s' not found", userName));
  }
}
