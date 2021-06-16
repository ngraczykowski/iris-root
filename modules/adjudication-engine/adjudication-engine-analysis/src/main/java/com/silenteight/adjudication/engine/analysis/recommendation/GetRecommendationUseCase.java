package com.silenteight.adjudication.engine.analysis.recommendation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.Recommendation;
import com.silenteight.adjudication.engine.common.resource.ResourceName;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
class GetRecommendationUseCase {

  private final GenerateCommentsUseCase generateCommentsUseCase;
  private final RecommendationRepository repository;

  Recommendation getRecommendation(String recommendationName) {
    if (log.isDebugEnabled()) {
      log.debug("Getting recommendation: recommendation={}", recommendationName);
    }

    return repository
        .getById(ResourceName.create(recommendationName).getLong("recommendations"))
        .toRecommendation();
  }
}
