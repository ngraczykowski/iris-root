package com.silenteight.bridge.core.registration.domain.model;

import lombok.Builder;

public record DefaultModelFeature(String name, String agentConfig) {

  @Builder
  public DefaultModelFeature {}
}
