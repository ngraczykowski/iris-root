package com.silenteight.adjudication.engine.solving.domain;

import lombok.Value;

import java.util.HashMap;
import java.util.Map;

@Value
class MatchFeatures {

  private final long matchId;
  private final Map<String, MatchFeature> features = new HashMap<>();

  public MatchFeatures addFeature(MatchFeature matchFeature) {
    this.features.put(matchFeature.getFeature(), matchFeature);
    return this;
  }
}
