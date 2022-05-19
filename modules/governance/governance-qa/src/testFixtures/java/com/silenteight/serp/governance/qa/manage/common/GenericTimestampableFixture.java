package com.silenteight.serp.governance.qa.manage.common;

import java.time.Instant;

import static java.lang.String.valueOf;
import static java.time.Instant.now;

public class GenericTimestampableFixture implements Tokenable {

  Instant addedAt = now();

  public String getToken() {
    return valueOf(addedAt);
  }
}
