package com.silenteight.simulator.dataset.domain.exception;

import lombok.NonNull;

import com.silenteight.simulator.dataset.domain.DatasetState;

import static java.lang.String.format;

public class DatasetNotInProperStateException extends RuntimeException {

  private static final long serialVersionUID = 2722908906716861145L;

  public DatasetNotInProperStateException(@NonNull DatasetState state) {
    super(format("Dataset should be in state: %s.", state));
  }
}
