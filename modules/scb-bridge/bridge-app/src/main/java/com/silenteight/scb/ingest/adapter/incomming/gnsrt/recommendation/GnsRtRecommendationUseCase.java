package com.silenteight.scb.ingest.adapter.incomming.gnsrt.recommendation;

import lombok.NonNull;

import com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.request.GnsRtRecommendationRequest;
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.response.GnsRtRecommendationResponse;

import reactor.core.publisher.Mono;

public interface GnsRtRecommendationUseCase {

  Mono<GnsRtRecommendationResponse> recommend(@NonNull GnsRtRecommendationRequest request);
}
