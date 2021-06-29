package com.silenteight.hsbc.bridge.recommendation.metadata;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class MatchMetadata {

  private String match;
  private String solution;
  private Map<String, String> reason = new HashMap<>();
  private Map<String, FeatureMetadata> features = new HashMap<>();
}
