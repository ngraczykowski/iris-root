package com.silenteight.serp.governance.qa.manage.domain.exception;

import java.util.UUID;

public class WrongAlertIdException extends RuntimeException {

  private static final long serialVersionUID = -6512962622453527186L;

  public WrongAlertIdException(UUID alertId) {
    super(String.format("Could not find alert with id=%s", alertId));
  }
}
