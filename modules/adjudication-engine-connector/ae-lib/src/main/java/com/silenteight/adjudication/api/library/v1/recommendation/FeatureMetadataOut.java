package com.silenteight.adjudication.api.library.v1.recommendation;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class FeatureMetadataOut {

  private String agentConfig;
  private String solution;
  private Map<String, String> reason = new HashMap<>();
}
