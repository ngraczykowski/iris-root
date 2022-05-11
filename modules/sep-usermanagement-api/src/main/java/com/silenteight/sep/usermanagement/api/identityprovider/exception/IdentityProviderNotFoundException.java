package com.silenteight.sep.usermanagement.api.identityprovider.exception;

import static java.lang.String.format;

public class IdentityProviderNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 4057391767115415012L;

  public IdentityProviderNotFoundException() {
    super(format("No Identity Provider found."));
  }

  public IdentityProviderNotFoundException(String providerAlias) {
    super(format("Identity Provider with alias=%s does not exists.", providerAlias));
  }
}
