package com.silenteight.bridge.core.recommendation.adapter.outgoing;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

interface CrudRecommendationRepository extends CrudRepository<RecommendationEntity, Long> {

  List<RecommendationEntity> findByAnalysisName(String analysisName);

  @Query("""
      SELECT recommended_action, COUNT(*) 
      FROM recommendations
      WHERE analysis_name = :analysisName
      GROUP BY recommended_action""")
  List<RecommendedActionStatisticsProjection> countRecommendationsByActionForAnalysisName(
      String analysisName);
}
