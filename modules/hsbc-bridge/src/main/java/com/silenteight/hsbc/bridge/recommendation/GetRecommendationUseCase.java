package com.silenteight.hsbc.bridge.recommendation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetRecommendationUseCase {

  private final RecommendationMapper recommendationMapper;
  private final RecommendationRepository repository;

  public RecommendationWithMetadataDto getRecommendation(@NonNull GetRecommendationRequest request) {
    var findResult = repository.findByAlert(request.getAlert());

    if (findResult.isEmpty()) {
      throw new RecommendationNotFoundException(request.getAlert());
    }

    var entity = findResult.get();
    var recommendation = recommendationMapper.getRecommendationValue(
        entity.getRecommendedAction(), request.getExtendedAttribute5());

    return RecommendationWithMetadataDto.builder()
        .alert(entity.getAlert())
        .recommendationComment(entity.getRecommendationComment())
        .recommendedAction(recommendation)
        .date(entity.getRecommendedAt())
        .name(entity.getName())
        .build();
  }

  public interface GetRecommendationRequest {

    String getAlert();

    String getExtendedAttribute5();
  }
}
