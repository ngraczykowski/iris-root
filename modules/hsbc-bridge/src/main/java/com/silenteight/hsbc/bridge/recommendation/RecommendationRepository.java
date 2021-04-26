package com.silenteight.hsbc.bridge.recommendation;

import org.springframework.data.repository.Repository;

import java.util.Optional;

interface RecommendationRepository extends Repository<RecommendationEntity, Long> {

  void save(RecommendationEntity entity);

  Optional<RecommendationEntity> findByAlert(String alert);

  boolean existsByName(String name);
}
