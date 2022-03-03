package com.silenteight.customerbridge.gnsrt.generator;

import lombok.NonNull;

import com.silenteight.customerbridge.gnsrt.model.request.GnsRtRecommendationRequest;

public interface GnsRtRequestGenerator {

  GnsRtRecommendationRequest generateBySystemId(@NonNull String systemId);

  GnsRtRecommendationRequest generateByRecordId(@NonNull String recordId);
}
