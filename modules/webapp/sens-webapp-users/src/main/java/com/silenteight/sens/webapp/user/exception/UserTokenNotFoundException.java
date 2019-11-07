package com.silenteight.sens.webapp.user.exception;

public class UserTokenNotFoundException extends RuntimeException {

  private static final long serialVersionUID = -7250163110606851695L;

  public UserTokenNotFoundException(String userName, String alias) {
    super(String.format("Cannot find token '%s' for user '%s'", alias, userName));
  }
}
