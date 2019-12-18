package com.silenteight.sens.webapp.backend.application.grpc.reco;

import lombok.RequiredArgsConstructor;

import com.silenteight.proto.serp.v1.api.GetRecommendationRequest;
import com.silenteight.proto.serp.v1.api.GetRecommendationResponse;
import com.silenteight.proto.serp.v1.api.RecommendationGrpc.RecommendationBlockingStub;

@RequiredArgsConstructor
class GrpcRecoClient implements RecoClient {

  private final RecommendationBlockingStub recoStub;

  @Override
  public GetRecommendationResponse getRecommendation(GetRecommendationRequest request) {
    return recoStub.getRecommendation(request);
  }
}
