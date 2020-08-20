package com.silenteight.sens.webapp.common.rest;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Authority {

  //TODO: ADMIN role is legacy and has been replaced by ADMINISTRATOR.
  //the ADMIN role will be removed at a later stage. (see: WA-926)
  public static final String ADMIN = "hasRole('ADMIN')";
  public static final String ADMINISTRATOR = "hasRole('ADMINISTRATOR')";
  public static final String ANALYST = "hasRole('ANALYST')";
  public static final String AUDITOR = "hasRole('AUDITOR')";
  public static final String BUSINESS_OPERATOR = "hasRole('BUSINESS_OPERATOR')";
  public static final String APPROVER = "hasRole('APPROVER')";

  public static final String APPROVER_OR_BUSINESS_OPERATOR =
      "hasAnyRole('APPROVER', 'BUSINESS_OPERATOR')";
  public static final String APPROVER_OR_AUDITOR_OR_BUSINESS_OPERATOR =
      "hasAnyRole('APPROVER', 'AUDITOR', 'BUSINESS_OPERATOR')";
  public static final String ANY_ROLE_EXCEPT_ANALYST =
      "hasAnyRole('ADMIN', 'AUDITOR', 'BUSINESS_OPERATOR', 'APPROVER', 'ADMINISTRATOR')";
  public static final String ADMIN_OR_ADMINISTRATOR =
      "hasAnyRole('ADMIN', 'ADMINISTRATOR')";
}
