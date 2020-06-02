package com.silenteight.sens.webapp.common.rest;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Authority {

  public static final String ADMIN = "hasRole('ADMIN')";
  public static final String ANALYST = "hasRole('ANALYST')";
  public static final String AUDITOR = "hasRole('AUDITOR')";
  public static final String BUSINESS_OPERATOR = "hasRole('BUSINESS_OPERATOR')";
  public static final String APPROVER = "hasRole('APPROVER')";

  // COMBINED_AUTHORITIES
  public static final String APPROVER_OR_BUSINESS_OPERATOR = APPROVER + " || " + BUSINESS_OPERATOR;
}
