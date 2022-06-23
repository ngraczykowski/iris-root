package com.silenteight.serp.governance.policy.domain.exception;

import lombok.NonNull;

import java.util.UUID;

public class EmptyFeatureConfiguration extends RuntimeException {

  private static final long serialVersionUID = 7373019308488485953L;

  public EmptyFeatureConfiguration(@NonNull UUID stepId) {
    super(String.format("In step %s an empty features configuration was requested.", stepId));
  }
}
