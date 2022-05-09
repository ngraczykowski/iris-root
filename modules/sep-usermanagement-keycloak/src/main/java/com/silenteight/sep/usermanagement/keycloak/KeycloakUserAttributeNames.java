package com.silenteight.sep.usermanagement.keycloak;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class KeycloakUserAttributeNames {

  public static final String USER_ORIGIN = "origin";
  public static final String LOCKED_AT = "deletedAt";
}
