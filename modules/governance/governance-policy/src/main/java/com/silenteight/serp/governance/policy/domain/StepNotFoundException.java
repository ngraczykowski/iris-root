package com.silenteight.serp.governance.policy.domain;

import java.util.UUID;

class StepNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 3507103740245061881L;

  StepNotFoundException(UUID stepId) {
    super("Policy Step not found: " + stepId);
  }
}
