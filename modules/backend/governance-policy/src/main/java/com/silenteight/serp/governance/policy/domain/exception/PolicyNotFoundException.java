package com.silenteight.serp.governance.policy.domain.exception;

import java.util.UUID;
import javax.persistence.EntityNotFoundException;

import static java.lang.String.format;

public class PolicyNotFoundException extends EntityNotFoundException {

  private static final long serialVersionUID = -3053402200780717786L;

  public PolicyNotFoundException(UUID policyId) {
    super(format("Policy with the id %s not found", policyId.toString()));
  }
}
