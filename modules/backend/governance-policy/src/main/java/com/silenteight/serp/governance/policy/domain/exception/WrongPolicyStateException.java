package com.silenteight.serp.governance.policy.domain.exception;

import com.silenteight.serp.governance.policy.domain.PolicyState;

import java.util.UUID;

public class WrongPolicyStateException extends RuntimeException {

  private static final long serialVersionUID = -7130488648716277097L;

  public WrongPolicyStateException(UUID policyId, PolicyState actual) {
    super(String.format("Could not manipulate Policy %s in state %s", policyId, actual));
  }
}
