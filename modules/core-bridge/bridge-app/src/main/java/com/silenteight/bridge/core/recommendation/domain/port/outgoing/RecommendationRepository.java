package com.silenteight.bridge.core.recommendation.domain.port.outgoing;

import com.silenteight.bridge.core.recommendation.domain.model.RecommendationWithMetadata;
import com.silenteight.bridge.core.recommendation.domain.model.RecommendedActionStatistics;

import java.util.List;

public interface RecommendationRepository {

  void saveAll(List<RecommendationWithMetadata> recommendations);

  List<RecommendationWithMetadata> findByAnalysisName(String analysisName);

  RecommendedActionStatistics countRecommendationsByActionForAnalysisName(String analysisName);
}
