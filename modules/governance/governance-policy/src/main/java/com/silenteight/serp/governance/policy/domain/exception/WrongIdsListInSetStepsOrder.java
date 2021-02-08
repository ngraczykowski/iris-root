package com.silenteight.serp.governance.policy.domain.exception;

import java.util.UUID;

public class WrongIdsListInSetStepsOrder extends RuntimeException {

  private static final long serialVersionUID = 1536123379617805255L;

  public WrongIdsListInSetStepsOrder(UUID policyId) {
    super(String.format("Could not set steps order. Requested steps order list does not contains "
                            + "all steps stored in policy %s.", policyId));
  }
}
