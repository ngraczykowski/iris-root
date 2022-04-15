package com.silenteight.simulator.dataset.domain.exception;

import java.util.UUID;

import static java.lang.String.format;

public class DatasetNotFoundException extends RuntimeException {

  private static final long serialVersionUID = -3337518887863522464L;

  public DatasetNotFoundException(UUID datasetId) {
    super(format("Dataset with datasetId=%s not found.", datasetId));
  }
}
