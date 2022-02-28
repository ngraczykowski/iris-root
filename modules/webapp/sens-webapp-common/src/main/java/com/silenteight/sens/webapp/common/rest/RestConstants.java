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

  public static final String OK_STATUS = "200";
  public static final String CREATED_STATUS = "201";
  public static final String ACCEPTED_STATUS = "202";
  public static final String NO_CONTENT_STATUS = "204";
  public static final String SUCCESS_RESPONSE_DESCRIPTION = "successful operation";
  public static final String BAD_REQUEST_STATUS = "400";
  public static final String BAD_REQUEST_DESCRIPTION = "bad request";
  public static final String NOT_FOUND_STATUS = "404";
  public static final String NOT_FOUND_DESCRIPTION = "entity not found";
  public static final String UNPROCESSABLE_ENTITY_STATUS = "422";
  public static final String INSUFFICIENT_STORAGE_STATUS = "507";
}
