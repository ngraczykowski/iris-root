package com.silenteight.bridge.core.recommendation.adapter.outgoing.jdbc;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

interface CrudRecommendationStatisticsRepository
    extends CrudRepository<RecommendationEntity, Long> {

  @Query("""
      SELECT recommended_action, COUNT(recommended_action)
      FROM core_bridge_recommendations
      WHERE analysis_name = :analysisName
      GROUP BY recommended_action
      """)
  List<RecommendationStatisticProjection> getRecommendationStatistics(String analysisName);


  @Query("""
      SELECT recommended_action, COUNT(recommended_action)
      FROM core_bridge_recommendations
      WHERE analysis_name = :analysisName
      AND alert_name IN (:alertsNames)
      GROUP BY recommended_action
      """)
  List<RecommendationStatisticProjection> getRecommendationStatistics(
      String analysisName, List<String> alertsNames);
}
