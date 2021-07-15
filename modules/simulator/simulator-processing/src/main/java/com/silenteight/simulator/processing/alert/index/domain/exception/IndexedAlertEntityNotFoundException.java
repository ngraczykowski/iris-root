package com.silenteight.simulator.processing.alert.index.domain.exception;

import lombok.NonNull;

import static java.lang.String.format;

public class IndexedAlertEntityNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 7755161991208886509L;

  public IndexedAlertEntityNotFoundException(@NonNull String requestId) {
    super(format("IndexedAlertEntity with requestId=%s not found.", requestId));
  }
}
