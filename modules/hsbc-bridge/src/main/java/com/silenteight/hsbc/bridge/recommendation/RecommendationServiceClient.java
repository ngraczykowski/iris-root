package com.silenteight.hsbc.bridge.recommendation;

import com.silenteight.hsbc.bridge.analysis.dto.GetRecommendationsDto;

import java.util.Collection;

public interface RecommendationServiceClient {

  Collection<RecommendationDto> getRecommendations(GetRecommendationsDto request);
}
