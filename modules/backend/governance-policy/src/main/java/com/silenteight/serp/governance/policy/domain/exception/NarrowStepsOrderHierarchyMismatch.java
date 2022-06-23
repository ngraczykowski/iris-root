package com.silenteight.serp.governance.policy.domain.exception;

import java.util.UUID;

import static java.lang.String.format;

public class NarrowStepsOrderHierarchyMismatch extends RuntimeException {

  private static final long serialVersionUID = 7094147928245251158L;
  private static final String MESSAGE_FORMAT = "Could not set steps order. Requested orders steps "
      + "mismatched order hierarchy. Narrow steps should be primary on the list in policy=%s.";

  public NarrowStepsOrderHierarchyMismatch(UUID policyId) {
    super(format(MESSAGE_FORMAT, policyId));
  }
}
