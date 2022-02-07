package com.silenteight.payments.bridge.firco.recommendation.model;

import lombok.Value;

import com.silenteight.payments.bridge.common.resource.ResourceName;

import java.util.UUID;

@Value
public class RecommendationId {

  UUID id;

  public static RecommendationId fromName(String name) {
    return new RecommendationId(ResourceName.create(name).getUuid("recommendations"));
  }
}
