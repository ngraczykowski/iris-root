package com.silenteight.sens.webapp.users.user.exception;


public class RoleNotFoundException extends RuntimeException {

  private static final long serialVersionUID = -6330820861213050664L;

  public RoleNotFoundException() {
    super("Cannot assign empty role");
  }
}
