package com.silenteight.bridge.core.recommendation.domain.exceptions;

import java.io.Serial;

public class CannotGetRecommendationsException extends RuntimeException {

  @Serial private static final long serialVersionUID = -5796896566434954500L;

  public CannotGetRecommendationsException(Throwable cause) {
    super(cause);
  }
}
