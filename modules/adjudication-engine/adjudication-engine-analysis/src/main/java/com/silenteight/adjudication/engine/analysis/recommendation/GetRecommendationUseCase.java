package com.silenteight.adjudication.engine.analysis.recommendation;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.Recommendation;
import com.silenteight.adjudication.engine.common.resource.ResourceName;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class GetRecommendationUseCase {

  private final GenerateCommentsUseCase generateCommentsUseCase;
  private final RecommendationRepository repository;

  Recommendation getRecommendation(String recommendationName) {
    // TODO(ahaczewski): Build Alert
    return repository
        .getById(ResourceName.create(recommendationName).getLong("recommendations"))
        .toRecommendation();
  }
}
