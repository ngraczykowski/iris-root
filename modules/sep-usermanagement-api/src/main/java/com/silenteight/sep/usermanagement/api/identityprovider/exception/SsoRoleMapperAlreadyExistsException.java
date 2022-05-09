package com.silenteight.sep.usermanagement.api.identityprovider.exception;

import static java.lang.String.format;

public class SsoRoleMapperAlreadyExistsException extends RuntimeException {

  private static final long serialVersionUID = 4457361227115415333L;

  public SsoRoleMapperAlreadyExistsException(String mapperName) {
    super(format("Role mapper with name=%s already exists.", mapperName));
  }
}
