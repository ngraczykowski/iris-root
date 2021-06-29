package com.silenteight.hsbc.bridge.recommendation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.recommendation.metadata.RecommendationMetadata;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@RequiredArgsConstructor
@Slf4j
class StoreRecommendationsUseCase {

  private final ObjectMapper objectMapper = new ObjectMapper();
  private final RecommendationRepository repository;

  @Transactional
  public void store(@NonNull Collection<RecommendationWithMetadataDto> recommendations) {
    recommendations.forEach(r -> {
      var name = r.getName();

      if (doesNotExist(name)) {
        save(r);

        log.debug("Recommendation stored, alert={}, recommendation={}", r.getAlert(), name);
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
    var metadataEntity = new RecommendationMetadataEntity(json);
    return metadataEntity;
  }

  private boolean doesNotExist(String name) {
    return !repository.existsByName(name);
  }
}
