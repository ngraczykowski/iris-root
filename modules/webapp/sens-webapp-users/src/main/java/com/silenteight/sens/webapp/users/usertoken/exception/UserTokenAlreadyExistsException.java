package com.silenteight.sens.webapp.users.usertoken.exception;

public class UserTokenAlreadyExistsException extends RuntimeException {

  private static final long serialVersionUID = -7420923719568555856L;

  public UserTokenAlreadyExistsException(String userName, String alias) {
    super(String.format("Token '%s' for user '%s' already exists", alias, userName));
  }
}
