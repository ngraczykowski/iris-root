package com.silenteight.hsbc.bridge.recommendation.metadata;

import lombok.Data;

import java.util.Map;

@Data
public class MatchMetadata {

  private String match;
  private String solution;
  private Map<String, String> reason;
  private Map<String, FeatureMetadata> features;
}
