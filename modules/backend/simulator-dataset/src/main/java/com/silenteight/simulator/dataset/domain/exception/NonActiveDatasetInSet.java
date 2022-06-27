package com.silenteight.simulator.dataset.domain.exception;

import java.util.Set;

import static java.lang.String.format;

public class NonActiveDatasetInSet extends RuntimeException {

  private static final long serialVersionUID = 7343824672680971627L;

  public NonActiveDatasetInSet(Set<String> datasets) {
    super(format("One of the provided datasets is not in an active state (%s).", datasets));
  }
}
