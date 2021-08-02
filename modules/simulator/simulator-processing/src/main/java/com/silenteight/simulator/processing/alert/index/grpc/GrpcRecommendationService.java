package com.silenteight.simulator.processing.alert.index.grpc;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v2.GetRecommendationRequest;
import com.silenteight.adjudication.api.v2.RecommendationServiceGrpc.RecommendationServiceBlockingStub;
import com.silenteight.adjudication.api.v2.RecommendationWithMetadata;
import com.silenteight.simulator.processing.alert.index.feed.RecommendationService;

@Slf4j
@RequiredArgsConstructor
class GrpcRecommendationService implements RecommendationService {

  @NonNull
  private final RecommendationServiceBlockingStub recommendationStub;

  @Override
  public RecommendationWithMetadata getRecommendationWithMetadata(
      @NonNull String recommendationName) {

    log.debug("Getting Recommendation with metadata by recommendationName={}", recommendationName);

    return recommendationStub.getRecommendationWithMetadata(
        toGetRecommendationRequest(recommendationName));
  }

  private static GetRecommendationRequest toGetRecommendationRequest(String recommendationName) {
    return GetRecommendationRequest.newBuilder()
        .setRecommendation(recommendationName)
        .build();
  }
}
