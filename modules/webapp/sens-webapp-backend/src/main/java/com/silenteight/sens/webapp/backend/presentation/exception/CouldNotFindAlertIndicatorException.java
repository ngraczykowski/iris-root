package com.silenteight.sens.webapp.backend.presentation.exception;

public class CouldNotFindAlertIndicatorException extends RuntimeException {

  private static final long serialVersionUID = 9049314155838756318L;

  private static final String MESSAGE = "Could not find alert indicator for unit %s and account %s";

  public CouldNotFindAlertIndicatorException(String unit, String account) {
    super(String.format(MESSAGE, unit, account));
  }
}
