package com.silenteight.sep.usermanagement.api.identityprovider.exception;

import static java.lang.String.format;

public class SsoRoleMapperNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 2057341227115615230L;

  public SsoRoleMapperNotFoundException(String roleMapperId) {
    super(format("Role mapper with id=%s not fund.", roleMapperId));
  }
}
