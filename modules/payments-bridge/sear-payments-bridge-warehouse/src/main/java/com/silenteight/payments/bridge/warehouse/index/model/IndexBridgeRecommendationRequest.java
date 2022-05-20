package com.silenteight.payments.bridge.warehouse.index.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class IndexBridgeRecommendationRequest {

  String discriminator;

  String systemId;

  String status;

  String reason;
}
