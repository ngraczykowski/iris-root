package com.silenteight.hsbc.bridge.recommendation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.recommendation.metadata.RecommendationMetadata;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
class StoreRecommendationsUseCase {

  private final ObjectMapper objectMapper;
  private final RecommendationRepository repository;

  public void store(@NonNull Collection<RecommendationWithMetadataDto> recommendations) {
    recommendations.forEach(r -> {
      try {
        save(r);
        if (log.isDebugEnabled()) {
          log.debug(
              "Recommendation stored, alert={}, recommendation={}", r.getAlert(), r.getName());
        }
      } catch (DataIntegrityViolationException e) {
        log.warn(
            "Recommendation already exists in DB, alert={}, recommendation={}",
            r.getAlert(),
            r.getName());
      }
    });
  }

  private void save(RecommendationWithMetadataDto recommendation) {
    var recommendationEntity = new RecommendationEntity(recommendation);
    var metadataEntity = createMetadataEntity(recommendation.getMetadata());
    recommendationEntity.setMetadata(metadataEntity);

    repository.save(recommendationEntity);
  }

  private RecommendationMetadataEntity createMetadataEntity(RecommendationMetadata metadata) {
    var json = objectMapper.valueToTree(metadata);
    return new RecommendationMetadataEntity(json);
  }
}
