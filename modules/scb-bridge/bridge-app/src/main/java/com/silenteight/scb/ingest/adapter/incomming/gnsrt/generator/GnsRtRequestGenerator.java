package com.silenteight.scb.ingest.adapter.incomming.gnsrt.generator;

import lombok.NonNull;

import com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.request.GnsRtRecommendationRequest;

public interface GnsRtRequestGenerator {

  GnsRtRecommendationRequest generateBySystemId(@NonNull String systemId);

  GnsRtRecommendationRequest generateByRecordId(@NonNull String recordId);

  GnsRtRecommendationRequest generateWithRandomSystemId(int numOfAlerts);
}
