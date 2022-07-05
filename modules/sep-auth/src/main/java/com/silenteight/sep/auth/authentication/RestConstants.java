package com.silenteight.sep.auth.authentication;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class RestConstants {

  public static final String ROOT = "/api";
  private static final String ISSUER_URI_SPRING_KEY =
      "${spring.security.oauth2.resourceserver.jwt.issuer-uri}";

  public static final String AUTHORIZATION_URL =
      ISSUER_URI_SPRING_KEY + "/protocol/openid-connect/auth";

  public static final String ACCESS_TOKEN_URL =
      ISSUER_URI_SPRING_KEY + "/protocol/openid-connect/token";

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
  public static final String CONFLICT_STATUS = "409";
  public static final String INSUFFICIENT_STORAGE_STATUS = "507";
}
