package com.silenteight.sens.webapp.backend.reasoningbranch.validate.exception;

import java.util.Collection;

public class FeatureVectorSignaturesNotFoundException extends RuntimeException {

  private static final long serialVersionUID = -7221490231830576152L;

  private final Collection<String> nonExistingFeatureVectorSignatures;

  public FeatureVectorSignaturesNotFoundException(
      Collection<String> nonExistingFeatureVectorSignatures) {
    this.nonExistingFeatureVectorSignatures = nonExistingFeatureVectorSignatures;
  }

  public Collection<String> getNonExistingFeatureVectorSignatures() {
    return nonExistingFeatureVectorSignatures;
  }
}
