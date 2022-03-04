package com.silenteight.agent.common.metrics;

import lombok.Value;

import static java.lang.String.format;

@Value
public class BucketBoundary {

  int value;

  BucketBoundary(int value) {
    validate(value);
    this.value = value;
  }

  private static void validate(int value) {
    if (value < 0) {
      throw new UnsupportedBucketBoundaryException(value);
    }
  }

  static class UnsupportedBucketBoundaryException extends RuntimeException {

    private static final long serialVersionUID = 2645980942109936363L;
    public static final String MESSAGE =
        "Expected boundary value must be equal or greater than 0, but was: %d.";

    UnsupportedBucketBoundaryException(int value) {
      super(format(MESSAGE, value));
    }
  }
}
