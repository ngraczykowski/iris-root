package com.silenteight.payments.bridge.firco.recommendation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v2.RecommendationMetadata;
import com.silenteight.payments.bridge.firco.recommendation.model.RecommendationId;
import com.silenteight.payments.bridge.firco.recommendation.model.RecommendationWrapper;
import com.silenteight.payments.bridge.firco.recommendation.port.CreateRecommendationUseCase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.AlternativeJdkIdGenerator;
import org.springframework.util.IdGenerator;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
class RecommendationService implements CreateRecommendationUseCase {

  private static final IdGenerator DEFAULT_ID_GENERATOR = new AlternativeJdkIdGenerator();

  private final RecommendationRepository recommendationRepository;
  private final RecommendationMetadataRepository recommendationMetadataRepository;
  private final ObjectMapper objectMapper;

  @Transactional
  public RecommendationId createRecommendation(RecommendationWrapper wrapper) {
    var entity = new RecommendationEntity(wrapper);

    var id = DEFAULT_ID_GENERATOR.generateId();
    entity.setId(id);

    var recommendationEntity = recommendationRepository.save(entity);

    wrapper.getMetadata()
        .flatMap(md -> createMetadata(md, recommendationEntity.getId()))
        .ifPresent(recommendationMetadataRepository::save);

    return new RecommendationId(id);
  }

  private Optional<RecommendationMetadataEntity> createMetadata(
      RecommendationMetadata metadata, UUID recommendationId) {
    try {
      var json = JsonFormat.printer().print(metadata);
      return convertToObjectNode(json)
          .map(node -> new RecommendationMetadataEntity(recommendationId, node));
    } catch (InvalidProtocolBufferException e) {
      log.warn("Could not convert metadata to JSON", e);
      return Optional.empty();
    }
  }

  private Optional<ObjectNode> convertToObjectNode(String json) {
    try {
      return Optional.of(objectMapper.readTree(json).deepCopy());
    } catch (JsonProcessingException e) {
      log.warn("Could not create ObjectNode from JSON", e);
      return Optional.empty();
    }
  }
}
