package com.silenteight.sens.webapp.backend.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Authority {

  // CLIENTS
  public static final String BACKEND_CLIENT =
      "'service-account-backend' == authentication.principal.username";
  public static final String REPORT_CLI_CLIENT =
      "'service-account-report-cli' == authentication.principal.username";
  public static final String POSTMAN_CLIENT =
      "'service-account-report-cli' == authentication.principal.username";

  // ROLES
  public static final String ADMIN = "hasRole('ADMIN') || " + POSTMAN_CLIENT;
  public static final String ANALYST = "hasRole('ANALYST') || " + POSTMAN_CLIENT;
  public static final String AUDITOR = "hasRole('AUDITOR') || " + POSTMAN_CLIENT;
  public static final String BUSINESS_OPERATOR =
      "hasRole('BUSINESS_OPERATOR') || " + POSTMAN_CLIENT;

  // COMBINED_AUTHORITIES
  public static final String AUDITOR_OR_REPORT_CLI =
      AUDITOR + " || " + REPORT_CLI_CLIENT;
  public static final String ADMIN_OR_BACKEND = ADMIN + " || " + BACKEND_CLIENT;
}
