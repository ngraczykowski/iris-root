package com.silenteight.hsbc.bridge.recommendation;

import com.silenteight.hsbc.bridge.analysis.dto.GetRecommendationsDto;

import java.util.Collection;

public interface RecommendationServiceClient {

  Collection<RecommendationDto> getRecommendations(GetRecommendationsDto request)
      throws CannotGetRecommendationsException;

  class CannotGetRecommendationsException extends RuntimeException {

    private static final String ERROR_MESSAGE = "Cannot get recommendation";

    private static final long serialVersionUID = 1433166629282519205L;

    public CannotGetRecommendationsException() {
      super(ERROR_MESSAGE);
    }
  }
}
