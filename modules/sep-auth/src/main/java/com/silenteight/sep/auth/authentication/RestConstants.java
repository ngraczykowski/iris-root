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
}
