package com.silenteight.warehouse.common.testing.rest;

import lombok.NoArgsConstructor;

import java.util.UUID;

import static java.util.UUID.fromString;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class TestCredentials {

  public static final String ELASTIC_ALLOWED_USERNAME = "businessuser";
  public static final String ELASTIC_ALLOWED_PASSWORD = "businessuser";
  public static final UUID ELASTIC_ALLOWED_ROLE =
      fromString("8326aa5a-cffc-4bc5-9f7f-9a4d94db616a");

  public static final String ELASTIC_FORBIDDEN_USERNAME = "bo";
  public static final String ELASTIC_FORBIDDEN_PASSWORD = "bodev";
}
