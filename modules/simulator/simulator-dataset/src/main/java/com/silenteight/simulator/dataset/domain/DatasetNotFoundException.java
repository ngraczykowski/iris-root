package com.silenteight.simulator.dataset.domain;

import lombok.NonNull;

import java.util.UUID;

public class DatasetNotFoundException extends RuntimeException {

  private static final long serialVersionUID = -3337518887863522464L;

  DatasetNotFoundException(@NonNull UUID datasetId) {
    super(String.format("Dataset with datasetName=%s not found.", datasetId.toString()));
  }
}
