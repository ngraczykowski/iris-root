package com.silenteight.bridge.core.recommendation.adapter.outgoing;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

interface CrudRecommendationRepository extends CrudRepository<RecommendationEntity, Long> {

  List<RecommendationEntity> findByAnalysisName(String analysisName);
}
