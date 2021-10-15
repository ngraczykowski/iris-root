package com.silenteight.payments.bridge.firco.recommendation.service;

import org.springframework.data.repository.Repository;

import java.util.UUID;

interface RecommendationMetadataRepository extends Repository<RecommendationMetadataEntity, UUID> {

  RecommendationMetadataEntity save(RecommendationMetadataEntity source);

}
