package com.silenteight.serp.governance.model.domain.exception;

import lombok.NonNull;

import java.util.UUID;

import static java.lang.String.format;

public class ModelNotFoundException extends RuntimeException {

  private static final long serialVersionUID = -6878602047053256900L;

  public ModelNotFoundException(@NonNull UUID modelId) {
    super(format("Model with modelId=%s not found.", modelId.toString()));
  }
}
