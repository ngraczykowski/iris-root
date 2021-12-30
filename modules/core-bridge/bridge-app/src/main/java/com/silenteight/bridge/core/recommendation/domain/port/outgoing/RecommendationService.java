package com.silenteight.bridge.core.recommendation.domain.port.outgoing;

import com.silenteight.bridge.core.recommendation.domain.model.RecommendationWithMetadata;

import java.util.List;

public interface RecommendationService {

  List<RecommendationWithMetadata> getRecommendations(String analysisName);
}
