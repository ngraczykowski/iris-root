package com.silenteight.bridge.core.recommendation.adapter.outgoing.jdbc;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

interface CrudRecommendationRepository extends CrudRepository<RecommendationEntity, Long> {

  List<RecommendationEntity> findByAnalysisName(String analysisName);

  @Query("""
      SELECT alert_name
      FROM core_bridge_recommendations
      WHERE analysis_name = :analysisName""")
  List<RecommendationAlertNameProjection> findAlertNamesByAnalysisName(String analysisName);

  List<RecommendationEntity> findByAnalysisNameAndAlertNameIn(
      String analysisName, List<String> alertNames);
}
