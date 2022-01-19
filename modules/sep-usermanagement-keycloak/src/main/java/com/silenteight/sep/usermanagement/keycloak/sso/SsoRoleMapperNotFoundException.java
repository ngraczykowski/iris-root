package com.silenteight.sep.usermanagement.keycloak.sso;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import com.silenteight.sep.usermanagement.keycloak.KeycloakException;

import static java.lang.String.format;

@EqualsAndHashCode(callSuper = true)
@ToString
public class SsoRoleMapperNotFoundException extends KeycloakException {

  private static final long serialVersionUID = 2057341227115615230L;

  public SsoRoleMapperNotFoundException(String mapperName) {
    super(format("Role mapper with name=%s not fund.", mapperName));
  }
}
