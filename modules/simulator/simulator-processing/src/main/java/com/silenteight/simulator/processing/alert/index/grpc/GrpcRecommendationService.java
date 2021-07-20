package com.silenteight.simulator.processing.alert.index.grpc;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.Recommendation;
import com.silenteight.adjudication.api.v2.GetRecommendationMetadataRequest;
import com.silenteight.adjudication.api.v2.GetRecommendationRequest;
import com.silenteight.adjudication.api.v2.RecommendationMetadata;
import com.silenteight.adjudication.api.v2.RecommendationServiceGrpc.RecommendationServiceBlockingStub;
import com.silenteight.simulator.processing.alert.index.feed.RecommendationService;

@RequiredArgsConstructor
class GrpcRecommendationService implements RecommendationService {

  private static final String METADATA_NAME_POSTFIX = "/metadata";

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

  @Override
  public RecommendationMetadata getMetadata(@NonNull String recommendationName) {
    return recommendationStub.getRecommendationMetadata(toGetMetadataRequest(recommendationName));
  }

  private static GetRecommendationMetadataRequest toGetMetadataRequest(String recommendationName) {
    return GetRecommendationMetadataRequest.newBuilder()
        .setRecommendationMetadata(toMetadataName(recommendationName))
        .build();
  }

  private static String toMetadataName(String recommendationName) {
    return recommendationName + METADATA_NAME_POSTFIX;
  }
}
