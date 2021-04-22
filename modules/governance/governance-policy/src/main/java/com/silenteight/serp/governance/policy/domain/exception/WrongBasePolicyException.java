package com.silenteight.serp.governance.policy.domain.exception;

import java.util.UUID;

public class WrongBasePolicyException extends RuntimeException {

  private static final long serialVersionUID = 5748749395771239045L;

  public WrongBasePolicyException(UUID policyId) {
    super(String.format("Could not find basePolicy with id=%s", policyId));
  }
}
