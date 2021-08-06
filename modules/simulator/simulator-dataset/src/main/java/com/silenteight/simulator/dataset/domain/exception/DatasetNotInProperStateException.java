package com.silenteight.simulator.dataset.domain.exception;

import lombok.NonNull;

import com.silenteight.simulator.dataset.domain.DatasetState;

import static java.lang.String.format;

public class DatasetNotInProperStateException extends RuntimeException {

  public DatasetNotInProperStateException(@NonNull DatasetState state) {
    super(format("Dataset should be in state: %s.", state));
  }
}
