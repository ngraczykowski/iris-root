package com.silenteight.serp.governance.policy.domain.exception;

import java.util.UUID;

public class StepsOrderListsSizeMismatch extends RuntimeException {

  private static final long serialVersionUID = -302980164643430781L;

  public StepsOrderListsSizeMismatch(UUID policyId, int requestStepsSize, long dbStepsSize) {
    super(String.format("Could not set steps order. Requested steps order list (%s) mismatch with "
                            + "the stored steps list size (%s) in policy %s.",
                        requestStepsSize, dbStepsSize, policyId));
  }
}
