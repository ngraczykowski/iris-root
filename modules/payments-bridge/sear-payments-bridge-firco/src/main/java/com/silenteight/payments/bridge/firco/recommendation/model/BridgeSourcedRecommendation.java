package com.silenteight.payments.bridge.firco.recommendation.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.common.model.AlertData;
import com.silenteight.payments.bridge.common.model.AlertId;
import com.silenteight.payments.bridge.warehouse.index.model.IndexBridgeRecommendationRequest;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class BridgeSourcedRecommendation implements AlertId {

  private final UUID alertId;
  private final String status;
  private final String reason;

  public IndexBridgeRecommendationRequest toIndexRecommendationRequest(AlertData alertData) {
    return IndexBridgeRecommendationRequest
        .builder()
        .discriminator(alertData.getDiscriminator())
        .reason(reason)
        .status(status)
        .systemId(alertData.getSystemId())
        .build();
  }
}
