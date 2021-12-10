package com.silenteight.recommendation.api.library.v1;

import java.io.Serial;

public class RecommendationLibraryException extends RuntimeException {

  @Serial private static final long serialVersionUID = 8733654935515612877L;

  public RecommendationLibraryException(String message, Throwable cause) {
    super(message, cause);
  }
}
