package com.silenteight.bridge.core.recommendation.domain.port.outgoing;

import com.silenteight.bridge.core.recommendation.domain.model.RecommendationWithMetadata;

import java.util.List;

public interface RecommendationRepository {

  void saveAll(List<RecommendationWithMetadata> recommendations);

  List<RecommendationWithMetadata> findByAnalysisName(String analysisName);

  List<String> findRecommendationAlertNamesByAnalysisName(String analysisName);

  List<RecommendationWithMetadata> findByAnalysisNameAndAlertNameIn(
      String analysisName, List<String> alertNames);
}
