package com.silenteight.serp.governance.qa.sampling.domain.exception;

import static java.lang.String.format;

public class WrongAlertSamplingIdException extends RuntimeException {

  private static final long serialVersionUID = -2704188321858303999L;

  public WrongAlertSamplingIdException(Long id) {
    super(format("Could not find alertSampling with id=%s", id));
  }
}
