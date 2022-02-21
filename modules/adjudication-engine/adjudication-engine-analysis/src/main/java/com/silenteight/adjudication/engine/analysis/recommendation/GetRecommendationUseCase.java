package com.silenteight.adjudication.engine.analysis.recommendation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.Recommendation;
import com.silenteight.adjudication.api.v2.RecommendationMetadata;
import com.silenteight.adjudication.api.v2.RecommendationWithMetadata;
import com.silenteight.adjudication.engine.analysis.recommendation.domain.AlertRecommendation;
import com.silenteight.adjudication.engine.common.resource.ResourceName;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
class GetRecommendationUseCase {

  private final RecommendationDataAccess recommendationDataAccess;

  @Timed(value = "ae.analysis.use_cases", extraTags = { "package", "recommendation" })
  Recommendation getRecommendation(String recommendationName) {
    log.debug("Getting recommendation: recommendation={}", recommendationName);

    AlertRecommendation alertRecommendation = getAlertRecommendation(recommendationName);

    return alertRecommendation.toRecommendation();
  }

  @Timed(value = "ae.analysis.use_cases", extraTags = { "package", "recommendation" })
  RecommendationMetadata getRecommendationMetadata(String metadataName) {
    log.debug("Getting recommendation metadata: recommendationMetadata={}", metadataName);

    AlertRecommendation alertRecommendation = getAlertRecommendation(metadataName);

    return alertRecommendation.toMetadata();
  }

  @Timed(value = "ae.analysis.use_cases", extraTags = { "package", "recommendation" })
  RecommendationWithMetadata getRecommendationWithMetadata(String recommendationName) {
    log.debug("Getting recommendation with metadata: recommendation={}", recommendationName);

    AlertRecommendation alertRecommendation = getAlertRecommendation(recommendationName);

    return alertRecommendation.toRecommendationWithMetadata();
  }

  private AlertRecommendation getAlertRecommendation(String metadataName) {
    var recommendationId = ResourceName.create(metadataName).getLong("recommendations");
    return recommendationDataAccess.getAlertRecommendation(recommendationId);
  }

}
