package com.silenteight.payments.bridge.firco.recommendation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v2.RecommendationMetadata;
import com.silenteight.payments.bridge.firco.recommendation.port.CreateRecommendationUseCase;
import com.silenteight.sep.base.common.exception.EntityNotFoundException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.protobuf.util.JsonFormat;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.AlternativeJdkIdGenerator;
import org.springframework.util.IdGenerator;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
class RecommendationService implements CreateRecommendationUseCase {

  private static final IdGenerator DEFAULT_ID_GENERATOR = new AlternativeJdkIdGenerator();
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
    var entity = new RecommendationEntity(recommendationWrapper);
    entity.setId(DEFAULT_ID_GENERATOR.generateId());

    RecommendationEntity recommendationEntity =
        recommendationRepository.save(entity);
    if (recommendationWrapper.hasRecommendation()) {
      recommendationMetadataRepository.save(
          createMetadata(recommendationWrapper.getRecommendationWithMetadata().getMetadata(),
              recommendationEntity.getId()));
    }

    return recommendationEntity;
  }

  private RecommendationMetadataEntity createMetadata(
      RecommendationMetadata metadata, UUID recommendationId) {
    try {
      var json = JsonFormat.printer().print(metadata);
      var node = (ObjectNode) objectMapper.readTree(json);
      return new RecommendationMetadataEntity(recommendationId, node);
    } catch (Exception exception) {
      log.error("", exception);
      throw new RuntimeException(exception);
    }
  }
}
