package com.silenteight.simulator.dataset.domain.exception;

import lombok.NonNull;

import java.util.UUID;

public class DatasetNotFoundException extends RuntimeException {

  private static final long serialVersionUID = -3337518887863522464L;

  public DatasetNotFoundException(@NonNull UUID datasetId) {
    super(String.format("Dataset with datasetId=%s not found.", datasetId.toString()));
  }
}
