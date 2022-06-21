package com.silenteight.serp.governance.model.domain.exception;

import lombok.NonNull;

import static java.lang.String.format;

public class TooManyModelsException extends RuntimeException {

  private static final long serialVersionUID = 7504029843469999104L;

  public TooManyModelsException(@NonNull String policy) {
    super(format("There are multiple moldes for policy=%s.", policy));
  }
}
