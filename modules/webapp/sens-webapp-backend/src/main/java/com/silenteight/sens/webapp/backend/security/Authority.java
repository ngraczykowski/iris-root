package com.silenteight.sens.webapp.backend.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Authority {

  public static final String ADMIN = "hasRole('ADMIN')";
  public static final String ANALYST = "hasRole('ANALYST')";
  public static final String AUDITOR = "hasRole('AUDITOR')";
  public static final String BUSINESS_OPERATOR = "hasRole('BUSINESS_OPERATOR')";
}
