package com.silenteight.customerbridge.gnsrt.recommendation;

import lombok.NonNull;

import com.silenteight.customerbridge.gnsrt.model.request.GnsRtRecommendationRequest;
import com.silenteight.customerbridge.gnsrt.model.response.GnsRtRecommendationResponse;

import reactor.core.publisher.Mono;

public interface GnsRtRecommendationUseCase {

  Mono<GnsRtRecommendationResponse> recommend(@NonNull GnsRtRecommendationRequest request);
}
