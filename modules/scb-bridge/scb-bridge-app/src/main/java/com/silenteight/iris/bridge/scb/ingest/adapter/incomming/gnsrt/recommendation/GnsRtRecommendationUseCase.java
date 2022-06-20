/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.recommendation;

import lombok.NonNull;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.model.request.GnsRtRecommendationRequest;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.model.response.GnsRtRecommendationResponse;

import reactor.core.publisher.Mono;

public interface GnsRtRecommendationUseCase {

  Mono<GnsRtRecommendationResponse> recommend(@NonNull GnsRtRecommendationRequest request);
}
