package com.silenteight.serp.governance.qa.manage.domain.exception;

import static java.lang.String.format;

public class WrongAlertIdException extends RuntimeException {

  private static final long serialVersionUID = -6512962622453527186L;

  public WrongAlertIdException(Long alertId) {
    super(format("Could not find alert with id=%s", alertId));
  }
}
