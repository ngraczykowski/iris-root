package com.silenteight.hsbc.bridge.recommendation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class RecommendationFacade {

  private final RecommendationRepository recommendationRepository;

  @Transactional
  public long storeRecommendation(@NonNull StoreRecommendationCommand command) {
    var entity = new RecommendationEntity(command);

    recommendationRepository.save(entity);
    return entity.getId();
  }
}
