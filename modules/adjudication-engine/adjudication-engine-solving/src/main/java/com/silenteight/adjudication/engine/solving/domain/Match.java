package com.silenteight.adjudication.engine.solving.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.common.resource.ResourceName;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Getter
public class Match implements Serializable {

  private static final long serialVersionUID = 4207894003659221746L;
  private final long matchId;
  private final Map<String, MatchFeature> features = new HashMap<>();
  String solution = null;
  String reason;

  public void addFeature(MatchFeature matchFeature) {
    this.features.put(matchFeature.getFeature(), matchFeature);
  }

  boolean hasAllFeaturesSolved() {
    return features.values().stream().allMatch(MatchFeature::hasValue);
  }

  void setSolution(String solution, String reason) {
    this.solution = solution;
    this.reason = reason;
  }


  boolean isSolved() {
    return solution != null;
  }

  public String getMatchName() {
    return ResourceName.create("").add("matches", matchId).getPath();
  }
}
