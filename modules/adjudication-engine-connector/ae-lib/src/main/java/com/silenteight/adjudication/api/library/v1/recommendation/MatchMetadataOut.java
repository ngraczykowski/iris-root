package com.silenteight.adjudication.api.library.v1.recommendation;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class MatchMetadataOut {

  private String match;
  private String solution;
  private Map<String, String> reason = new HashMap<>();
  private Map<String, String> categories = new HashMap<>();
  private Map<String, FeatureMetadataOut> features = new HashMap<>();
}
