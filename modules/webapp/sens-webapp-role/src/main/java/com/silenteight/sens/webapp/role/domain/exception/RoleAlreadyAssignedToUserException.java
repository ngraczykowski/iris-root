package com.silenteight.sens.webapp.role.domain.exception;

import static java.lang.String.format;

public class RoleAlreadyAssignedToUserException extends RuntimeException {

  private static final long serialVersionUID = -5812110563437174332L;

  public RoleAlreadyAssignedToUserException(String roleName) {
    super(format("There are users with role %s. Role will not be removed.", roleName));
  }
}
