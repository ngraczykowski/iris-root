package com.silenteight.bridge.core.registration.domain.model;

import lombok.Builder;

import java.util.List;

public record DefaultModel(String name,
                           String policyName,
                           String strategyName,
                           List<DefaultModelFeature> features,
                           List<String> categories) {

  @Builder
  public DefaultModel {}
}
