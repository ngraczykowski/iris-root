package com.silenteight.payments.bridge.firco.recommendation.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.firco.recommendation.model.Recommendation;
import com.silenteight.payments.bridge.firco.recommendation.model.RecommendationId;
import com.silenteight.payments.bridge.firco.recommendation.port.GetRecommendationUseCase;
import com.silenteight.sep.base.aspects.metrics.Timed;
import com.silenteight.sep.base.common.exception.EntityNotFoundException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;

@Service
@RequiredArgsConstructor
class GetRecommendationService implements GetRecommendationUseCase {

  private final RecommendationRepository repository;

  @Override
  @Timed(percentiles = { 0.5, 0.95, 0.99}, histogram = true)
  public Recommendation get(RecommendationId recommendation) {
    return repository
        .findById(recommendation.getId())
        .map(entity -> new Recommendation(
            mapAction(entity.getAction()), mapComment(entity.getComment())))
        .orElseThrow(EntityNotFoundException::new);
  }

  private static String mapAction(@Nullable String action) {
    return StringUtils.isNotBlank(action) ? action : "ACTION_INVESTIGATE";
  }

  private static String mapComment(@Nullable String comment) {
    return StringUtils.isNotBlank(comment)
           ? comment
           : "S8 recommended action: Manual Investigation";
  }
}
