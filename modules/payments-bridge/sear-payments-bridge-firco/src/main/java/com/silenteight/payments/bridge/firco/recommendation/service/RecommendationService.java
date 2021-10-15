package com.silenteight.payments.bridge.firco.recommendation.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v2.RecommendationMetadata;
import com.silenteight.payments.bridge.firco.recommendation.port.CreateRecommendationUseCase;
import com.silenteight.sep.base.common.exception.EntityNotFoundException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
class RecommendationService implements CreateRecommendationUseCase {

  private final RecommendationRepository recommendationRepository;
  private final RecommendationMetadataRepository recommendationMetadataRepository;
  private final ObjectMapper objectMapper;

  public RecommendationEntity findById(UUID recommendationId) {
    return recommendationRepository
        .findById(recommendationId)
        .orElseThrow(EntityNotFoundException::new);
  }

  @Transactional
  public RecommendationEntity createRecommendation(RecommendationWrapper recommendationWrapper) {
    RecommendationEntity recommendationEntity =
        recommendationRepository.save(new RecommendationEntity(recommendationWrapper));
    if (recommendationWrapper.hasRecommendation()) {
      recommendationMetadataRepository.save(
          createMetadata(recommendationWrapper.getRecommendationWithMetadata().getMetadata(),
              recommendationEntity.getId()));
    }
    return recommendationEntity;
  }

  private RecommendationMetadataEntity createMetadata(
      RecommendationMetadata metadata, UUID recommendationId) {
    var originalMessage = objectMapper.convertValue(metadata, ObjectNode.class);
    return new RecommendationMetadataEntity(recommendationId, originalMessage);
  }
}
