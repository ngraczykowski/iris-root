package com.silenteight.sens.webapp.user.domain;

public class ExternalOrigin implements UserOrigin {

  public static final String EXTERNAL_ORIGIN = "EXTERNAL";

  @Override
  public String toString() {
    return EXTERNAL_ORIGIN;
  }
}
