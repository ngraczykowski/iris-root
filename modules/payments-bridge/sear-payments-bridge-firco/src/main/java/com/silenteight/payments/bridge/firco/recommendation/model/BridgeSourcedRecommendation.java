package com.silenteight.payments.bridge.firco.recommendation.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.common.model.AlertId;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class BridgeSourcedRecommendation implements AlertId {

  private final UUID alertId;
  private final String status;
  private final String reason;

}
