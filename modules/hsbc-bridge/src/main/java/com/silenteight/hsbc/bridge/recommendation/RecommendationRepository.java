package com.silenteight.hsbc.bridge.recommendation;

import org.springframework.data.repository.Repository;

interface RecommendationRepository extends Repository<RecommendationEntity, Long> {

  void save(RecommendationEntity entity);
}
