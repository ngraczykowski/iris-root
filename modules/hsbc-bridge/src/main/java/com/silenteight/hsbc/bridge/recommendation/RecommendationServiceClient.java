package com.silenteight.hsbc.bridge.recommendation;

import java.util.Collection;

public interface RecommendationServiceClient {

  Collection<RecommendationWithMetadataDto> getRecommendations(String analysis)
      throws CannotGetRecommendationsException;

  class CannotGetRecommendationsException extends RuntimeException {

    private static final String ERROR_MESSAGE = "Cannot get recommendation";

    private static final long serialVersionUID = 1433166629282519205L;

    public CannotGetRecommendationsException(Throwable cause) {
      super(ERROR_MESSAGE, cause);
    }
  }
}
