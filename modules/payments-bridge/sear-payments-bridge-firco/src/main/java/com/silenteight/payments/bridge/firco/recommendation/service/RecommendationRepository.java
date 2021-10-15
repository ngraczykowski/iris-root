package com.silenteight.payments.bridge.firco.recommendation.service;

import org.springframework.data.repository.Repository;

import java.util.Optional;
import java.util.UUID;

interface RecommendationRepository extends Repository<RecommendationEntity, UUID> {

  RecommendationEntity save(RecommendationEntity recommendation);

  Optional<RecommendationEntity> findById(UUID recommendationId);

}
