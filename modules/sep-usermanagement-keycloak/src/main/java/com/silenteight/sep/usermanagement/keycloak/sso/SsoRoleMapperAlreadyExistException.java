package com.silenteight.sep.usermanagement.keycloak.sso;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import com.silenteight.sep.usermanagement.keycloak.KeycloakException;

import static java.lang.String.format;

@EqualsAndHashCode(callSuper = true)
@ToString
public class SsoRoleMapperAlreadyExistException extends KeycloakException {

  private static final long serialVersionUID = 4457361227115415333L;

  public SsoRoleMapperAlreadyExistException(String mapperName) {
    super(format("Role mapper with name=%s already exists.", mapperName));
  }
}
