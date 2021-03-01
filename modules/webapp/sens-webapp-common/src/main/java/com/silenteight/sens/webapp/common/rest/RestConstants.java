package com.silenteight.sens.webapp.common.rest;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RestConstants {

  public static final String ROOT = "/api";
  public static final String MANAGEMENT_PREFIX = "/management";
  public static final String OPENAPI_PREFIX = "/openapi";
  public static final String AUTH_CONFIGURATION_PREFIX = "/configuration/auth";
  public static final String CORRELATION_ID_HEADER = "CorrelationId";
}
