package com.silenteight.sep.usermanagement.keycloak.sso;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import com.silenteight.sep.usermanagement.keycloak.KeycloakException;

import static java.lang.String.format;

@EqualsAndHashCode(callSuper = true)
@ToString
public class IdentityProviderNotFoundException extends KeycloakException {

  private static final long serialVersionUID = 4057391767115415012L;

  public IdentityProviderNotFoundException() {
    super(format("No Identity Provider found."));
  }

  IdentityProviderNotFoundException(String providerAlias) {
    super(format("Identity Provider with alias=%s does not exists.", providerAlias));
  }
}
