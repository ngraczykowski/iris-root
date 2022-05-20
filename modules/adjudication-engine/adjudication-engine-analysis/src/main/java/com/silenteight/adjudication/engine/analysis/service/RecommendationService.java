package com.silenteight.adjudication.engine.analysis.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.Recommendation;
import com.silenteight.adjudication.api.v2.*;
import com.silenteight.adjudication.engine.analysis.recommendation.RecommendationFacade;

import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@RequiredArgsConstructor
@Service
class RecommendationService {

  private final RecommendationFacade recommendationFacade;

  void streamRecommendations(
      StreamRecommendationsRequest request, Consumer<Recommendation> onNext) {

    recommendationFacade.streamRecommendations(request.getRecommendationSource(), onNext);
  }

  void streamRecommendationsWithMetadata(
      StreamRecommendationsWithMetadataRequest request,
      Consumer<RecommendationWithMetadata> onNext) {

    recommendationFacade.streamRecommendationsWithMetadata(
        request.getRecommendationSource(), onNext);
  }

  Recommendation getRecommendation(GetRecommendationRequest request) {
    return recommendationFacade.getRecommendation(request.getRecommendation());
  }

  RecommendationMetadata getRecommendationMetadata(GetRecommendationMetadataRequest request) {
    return recommendationFacade.getRecommendationMetadata(request.getRecommendationMetadata());
  }

  RecommendationWithMetadata getRecommendationWithMetadata(GetRecommendationRequest request) {
    return recommendationFacade.getRecommendationWithMetadata(request.getRecommendation());
  }
}
