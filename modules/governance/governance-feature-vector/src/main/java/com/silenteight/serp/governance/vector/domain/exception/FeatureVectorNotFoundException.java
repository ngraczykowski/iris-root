package com.silenteight.serp.governance.vector.domain.exception;

import javax.persistence.EntityNotFoundException;

public class FeatureVectorNotFoundException extends EntityNotFoundException {

  private static final long serialVersionUID = 1601950751487977275L;

  public FeatureVectorNotFoundException(String signature) {
    super(String.format("Could not find FV with the signature %s", signature));
  }
}
