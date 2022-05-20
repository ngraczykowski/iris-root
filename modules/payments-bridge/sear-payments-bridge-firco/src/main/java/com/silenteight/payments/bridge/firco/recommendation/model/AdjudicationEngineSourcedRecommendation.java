package com.silenteight.payments.bridge.firco.recommendation.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v2.RecommendationWithMetadata;
import com.silenteight.payments.bridge.common.model.AlertId;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class AdjudicationEngineSourcedRecommendation implements AlertId {

  private final UUID alertId;
  private final RecommendationWithMetadata recommendation;

  public String getAlertName() {
    return getRecommendation().getRecommendation().getAlert();
  }

}
