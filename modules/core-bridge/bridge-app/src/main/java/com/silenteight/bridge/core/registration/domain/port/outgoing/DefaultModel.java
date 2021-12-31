package com.silenteight.bridge.core.registration.domain.port.outgoing;

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
