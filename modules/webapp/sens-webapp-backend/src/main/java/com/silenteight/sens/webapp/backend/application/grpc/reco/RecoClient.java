package com.silenteight.sens.webapp.backend.application.grpc.reco;

import com.silenteight.proto.serp.v1.api.GetRecommendationRequest;
import com.silenteight.proto.serp.v1.api.GetRecommendationResponse;

public interface RecoClient {

  GetRecommendationResponse getRecommendation(GetRecommendationRequest request);
}
