package com.silenteight.sens.webapp.role.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DomainConstants {

  public static final int ROLE_FIELD_MIN_LENGTH = 3;
  public static final int ROLE_FIELD_MAX_LENGTH = 80;

  public static final String ROLE_ENDPOINT_TAG = "Role";
}
