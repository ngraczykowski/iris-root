package com.silenteight.serp.governance.model.domain.exception;

import lombok.NonNull;

import static java.lang.String.format;

public class TooManyModelsException extends RuntimeException {

  public TooManyModelsException(@NonNull String policy) {
    super(format("There are multiple moldes for policy=%s.", policy));
  }
}
