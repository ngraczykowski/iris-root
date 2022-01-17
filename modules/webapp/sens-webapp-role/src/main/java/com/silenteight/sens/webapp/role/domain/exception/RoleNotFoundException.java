package com.silenteight.sens.webapp.role.domain.exception;

import java.util.UUID;

import static java.lang.String.format;

public class RoleNotFoundException extends RuntimeException {

  private static final long serialVersionUID = -272645148762587064L;

  public RoleNotFoundException(UUID roleId) {
    super(format("Role with roleId=%s not found.", roleId));
  }
}
