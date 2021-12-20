package com.silenteight.bridge.core.registration.domain.port.outgoing;

import lombok.Builder;

public record DefaultModelFeature(String name, String agentConfig) {

  @Builder
  public DefaultModelFeature {}
}
