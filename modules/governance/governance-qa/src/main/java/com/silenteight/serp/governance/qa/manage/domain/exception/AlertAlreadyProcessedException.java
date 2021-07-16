package com.silenteight.serp.governance.qa.manage.domain.exception;

import static java.lang.String.format;

public class AlertAlreadyProcessedException extends RuntimeException {

  private static final long serialVersionUID = 440927469968218054L;

  public AlertAlreadyProcessedException(String discriminator) {
    super(format("Alert with with discriminator=%s already processed", discriminator));
  }
}
