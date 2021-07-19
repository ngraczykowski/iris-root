package com.silenteight.simulator.processing.alert.index.grpc;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.Recommendation;
import com.silenteight.adjudication.api.v2.GetRecommendationRequest;
import com.silenteight.adjudication.api.v2.RecommendationServiceGrpc.RecommendationServiceBlockingStub;
import com.silenteight.simulator.processing.alert.index.feed.RecommendationService;

@RequiredArgsConstructor
class GrpcRecommendationService implements RecommendationService {

  @NonNull
  private final RecommendationServiceBlockingStub recommendationStub;

  @Override
  public Recommendation getRecommendation(@NonNull String recommendationName) {
    return recommendationStub.getRecommendation(toGetRecommendationRequest(recommendationName));
  }

  private static GetRecommendationRequest toGetRecommendationRequest(String recommendationName) {
    return GetRecommendationRequest.newBuilder()
        .setRecommendation(recommendationName)
        .build();
  }
}
