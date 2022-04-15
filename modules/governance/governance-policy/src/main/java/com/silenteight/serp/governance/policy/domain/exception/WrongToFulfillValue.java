package com.silenteight.serp.governance.policy.domain.exception;

import lombok.NonNull;

import java.util.UUID;

public class WrongToFulfillValue extends RuntimeException {

  private static final long serialVersionUID = -6292146088881916236L;

  private static final String MESSAGE = "In step %s, field 'toFulfill' have value %d, "
      + "but allowed value is between 1 and %d.";

  public WrongToFulfillValue(@NonNull UUID stepId, int toFulfill, int maxSize) {
    super(String.format(MESSAGE, stepId, toFulfill, maxSize));
  }
}
