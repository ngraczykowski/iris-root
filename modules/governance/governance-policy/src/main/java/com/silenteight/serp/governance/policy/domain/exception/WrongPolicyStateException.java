package com.silenteight.serp.governance.policy.domain.exception;

import com.silenteight.serp.governance.policy.domain.PolicyState;

import java.util.UUID;

public class WrongPolicyStateException extends RuntimeException {

  private static final long serialVersionUID = -7130488648716277097L;

  public WrongPolicyStateException(UUID policyId, PolicyState actual) {
    super(String.format("Could not edit Policy in state %s in policy %s", actual, policyId));
  }
}
