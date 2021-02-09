package com.silenteight.serp.governance.policy.domain.exception;

import lombok.NonNull;

import java.util.UUID;

public class EmptyFeaturesLogicConfiguration extends RuntimeException {

  private static final long serialVersionUID = 1683638662741036323L;

  public EmptyFeaturesLogicConfiguration(@NonNull UUID stepId) {
    super(String.format("In step %s an empty feature logic configuration was requested.", stepId));
  }
}
