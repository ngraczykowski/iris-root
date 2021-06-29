package com.silenteight.adjudication.engine.analysis.recommendation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.Recommendation;
import com.silenteight.adjudication.api.v2.RecommendationMetadata;
import com.silenteight.adjudication.engine.analysis.recommendation.domain.GenerateCommentsRequest;
import com.silenteight.adjudication.engine.common.resource.ResourceName;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
class GetRecommendationUseCase {

  private final GenerateCommentsUseCase generateCommentsUseCase;
  private final RecommendationDataAccess recommendationDataAccess;

  Recommendation getRecommendation(String recommendationName) {
    log.debug("Getting recommendation: recommendation={}", recommendationName);

    var recommendationId = ResourceName.create(recommendationName).getLong("recommendations");
    var alertRecommendation = recommendationDataAccess.getAlertRecommendation(recommendationId);
    var commentsResponse = generateCommentsUseCase.generateComments(
        new GenerateCommentsRequest(alertRecommendation.getAlertContext()));

    return alertRecommendation.toRecommendation(commentsResponse.getComment());
  }

  RecommendationMetadata getRecommendationMetadata(String metadataName) {
    log.debug("Getting recommendation metadata: recommendationMetadata={}", metadataName);

    var recommendationId = ResourceName.create(metadataName).getLong("recommendations");
    var alertRecommendation = recommendationDataAccess.getAlertRecommendation(recommendationId);

    return alertRecommendation.toMetadata();
  }
}
