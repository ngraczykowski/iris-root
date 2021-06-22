package com.silenteight.warehouse.common.testing.rest;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class TestCredentials {

  public static final String ELASTIC_ALLOWED_USERNAME = "businessuser";
  public static final String ELASTIC_ALLOWED_PASSWORD = "businessuser";

  public static final String ELASTIC_FORBIDDEN_USERNAME = "bo";
  public static final String ELASTIC_FORBIDDEN_PASSWORD = "bodev";
}
