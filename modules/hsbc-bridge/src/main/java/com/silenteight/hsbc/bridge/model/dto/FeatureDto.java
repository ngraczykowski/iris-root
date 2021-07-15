package com.silenteight.hsbc.bridge.model.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class FeatureDto {

  String name;
  String agentConfig;
}
