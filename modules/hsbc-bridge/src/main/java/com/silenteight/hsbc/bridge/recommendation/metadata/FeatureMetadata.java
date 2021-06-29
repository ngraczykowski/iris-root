package com.silenteight.hsbc.bridge.recommendation.metadata;

import lombok.Data;

import java.util.Map;

@Data
public class FeatureMetadata {

  private String agentConfig;
  private String solution;
  private Map<String, String> reason;
}
