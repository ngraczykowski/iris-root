package com.silenteight.serp.governance.policy.domain.exception;

import java.util.UUID;

public class StepNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 3507103740245061881L;

  public StepNotFoundException(UUID stepId) {
    super("Policy Step not found: " + stepId);
  }
}
