package com.silenteight.serp.governance.policy.domain.exception;

import com.silenteight.serp.governance.policy.domain.PolicyState;

import java.util.UUID;

public class WrongPolicyStateChangeException extends RuntimeException {

  private static final long serialVersionUID = -7130488648716277097L;

  public WrongPolicyStateChangeException(UUID policyId, PolicyState actual, PolicyState desirable) {
    super(String.format("Could not change Policy state from %s to %s in policy %s",
                        actual, desirable, policyId));
  }
}
