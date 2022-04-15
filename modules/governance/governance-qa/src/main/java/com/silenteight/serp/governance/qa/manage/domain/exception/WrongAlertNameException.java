package com.silenteight.serp.governance.qa.manage.domain.exception;

import static java.lang.String.format;

public class WrongAlertNameException extends RuntimeException {

  private static final long serialVersionUID = -4053597311211083842L;

  public WrongAlertNameException(String alertName) {
    super(format("Could not find alert with alertName=%s", alertName));
  }
}
