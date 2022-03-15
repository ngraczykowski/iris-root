package com.silenteight.recommendation.api.library.v1;

import java.io.Serializable;

public class RecommendationLibraryException extends RuntimeException implements Serializable {

  private static final long serialVersionUID = 8733654935515612877L;

  public RecommendationLibraryException(String message, Throwable cause) {
    super(message, cause);
  }
}
