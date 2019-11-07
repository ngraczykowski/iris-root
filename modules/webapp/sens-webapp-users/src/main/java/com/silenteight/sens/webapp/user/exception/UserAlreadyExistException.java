package com.silenteight.sens.webapp.user.exception;

public class UserAlreadyExistException extends RuntimeException {

  private static final long serialVersionUID = 3624942162396510233L;

  public UserAlreadyExistException(String userName) {
    super(String.format("User with name '%s' already exists", userName));
  }
}
