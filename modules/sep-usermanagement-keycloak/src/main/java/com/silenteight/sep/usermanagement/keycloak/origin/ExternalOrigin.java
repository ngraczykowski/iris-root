package com.silenteight.sep.usermanagement.keycloak.origin;

import com.silenteight.sep.usermanagement.api.origin.UserOrigin;

public class ExternalOrigin implements UserOrigin {

  public static final String EXTERNAL_ORIGIN = "EXTERNAL";

  @Override
  public String toString() {
    return EXTERNAL_ORIGIN;
  }
}
