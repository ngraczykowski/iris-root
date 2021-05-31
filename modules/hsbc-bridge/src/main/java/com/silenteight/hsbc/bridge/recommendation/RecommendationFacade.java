package com.silenteight.hsbc.bridge.recommendation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RecommendationFacade {

  private final RecommendationRepository repository;

  public RecommendationDto getRecommendation(@NonNull String alert) {
    var findResult = repository.findByAlert(alert);

    if (findResult.isEmpty()) {
      throw new RecommendationNotFoundException(alert);
    }

    var entity = findResult.get();
    return RecommendationDto.builder()
        .alert(entity.getAlert())
        .recommendationComment(entity.getRecommendationComment())
        .recommendedAction(entity.getRecommendedAction())
        .date(entity.getRecommendedAt())
        .name(entity.getName())
        .build();
  }
}
