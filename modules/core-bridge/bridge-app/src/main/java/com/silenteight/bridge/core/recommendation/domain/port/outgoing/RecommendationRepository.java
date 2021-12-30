package com.silenteight.bridge.core.recommendation.domain.port.outgoing;

import com.silenteight.bridge.core.recommendation.domain.model.RecommendationWithMetadata;

import java.util.List;

public interface RecommendationRepository {

  void saveAll(List<RecommendationWithMetadata> recommendations);

  List<RecommendationWithMetadata> findByAnalysisName(String analysisName);
}
