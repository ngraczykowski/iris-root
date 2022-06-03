package com.silenteight.payments.bridge.firco.recommendation.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.common.indexing.DiscriminatorStrategy;
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

  public IndexBridgeRecommendationRequest toIndexRecommendationRequest(
      DiscriminatorStrategy discriminatorStrategy, AlertData alertData) {
    var discriminator =
        discriminatorStrategy.create(
            alertData.getAlertId().toString(), alertData.getSystemId(), alertData.getMessageId());
    return IndexBridgeRecommendationRequest.builder()
        .discriminator(discriminator)
        .reason(reason)
        .status(status)
        .systemId(alertData.getSystemId())
        .build();
  }
}
