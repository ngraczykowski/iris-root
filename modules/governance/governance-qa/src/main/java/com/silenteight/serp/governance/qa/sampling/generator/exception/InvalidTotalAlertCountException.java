package com.silenteight.serp.governance.qa.sampling.generator.exception;

import static java.lang.String.format;

public class InvalidTotalAlertCountException extends RuntimeException {

  private static final long serialVersionUID = -2460663498040286781L;

  public InvalidTotalAlertCountException(Long totalAlertCount) {
    super(format("Invalid total alert count=%d", totalAlertCount));
  }
}
