package com.silenteight.serp.governance.qa.domain.exception;

public class WrongAlertNameException extends RuntimeException {

  private static final long serialVersionUID = -4053597311211083842L;

  public WrongAlertNameException(String alertName) {
    super(String.format("Could not find alert with name=%s", alertName));
  }
}
