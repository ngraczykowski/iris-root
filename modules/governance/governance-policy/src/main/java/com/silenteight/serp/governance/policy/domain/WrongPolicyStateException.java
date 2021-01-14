package com.silenteight.serp.governance.policy.domain;

import java.util.UUID;

public class WrongPolicyStateException extends RuntimeException {

  private static final long serialVersionUID = -7130488648716277097L;

  public WrongPolicyStateException(UUID policyId, PolicyState actual, PolicyState desirable) {
    super(String.format("Could not change Policy state from %s to %s in policy %s",
                        actual, desirable, policyId));
  }
}
