package com.silenteight.simulator.common.web.rest;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RestConstants {

  public static final String CORRELATION_ID_HEADER = "CorrelationId";
  public static final String MANAGEMENT_PREFIX = "/management";
  public static final String OPENAPI_PREFIX = "/openapi";
  public static final String ROOT = "/api";
}
