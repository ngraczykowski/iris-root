package com.silenteight.governance.api.library.v1.model;

import lombok.Builder;
import lombok.Value;

import com.silenteight.model.api.v1.Feature;

@Value
@Builder
public class FeatureOut {

  String name;
  String agentConfig;

  static FeatureOut createFrom(Feature feature) {
    return FeatureOut.builder()
        .name(feature.getName())
        .agentConfig(feature.getAgentConfig())
        .build();
  }
}
