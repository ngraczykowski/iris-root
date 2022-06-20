/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.generator;

import lombok.NonNull;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.model.request.GnsRtRecommendationRequest;

public interface GnsRtRequestGenerator {

  GnsRtRecommendationRequest generateBySystemId(@NonNull String systemId);

  GnsRtRecommendationRequest generateByRecordId(@NonNull String recordId);

  GnsRtRecommendationRequest generateWithRandomSystemId(int numOfAlerts);
}
