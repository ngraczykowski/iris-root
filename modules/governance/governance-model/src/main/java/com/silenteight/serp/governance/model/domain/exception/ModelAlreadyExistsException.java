package com.silenteight.serp.governance.model.domain.exception;

import lombok.NonNull;

import java.util.UUID;

import static java.lang.String.format;

public class ModelAlreadyExistsException extends RuntimeException {

  private static final long serialVersionUID = -7741376825489814924L;

  public ModelAlreadyExistsException(@NonNull UUID modelId, @NonNull String policyName) {
    super(format(
        "Model (modelId=%s) for policy=%s already exists.", modelId.toString(), policyName));
  }
}
