package com.silenteight.hsbc.bridge.analysis.dto;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class FeatureDto {

  String name;
  String agentConfig;
}
