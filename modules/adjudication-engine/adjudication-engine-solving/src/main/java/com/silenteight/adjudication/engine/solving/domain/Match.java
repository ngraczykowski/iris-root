package com.silenteight.adjudication.engine.solving.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.common.resource.ResourceName;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Getter
@Slf4j
public class Match implements Serializable {

  private static final long serialVersionUID = 4207894003659221746L;
  private final long matchId;
  private final String clientMatchId;
  private final Map<String, MatchFeature> features = new HashMap<>();
  private final Map<String, MatchCategory> categories = new HashMap<>();
  String solution = null;
  String reason;

  public void addFeature(MatchFeature matchFeature) {
    if (matchFeature.getFeature() == null) {
      return;
    }
    this.features.put(matchFeature.getFeature(), matchFeature);
  }

  public void addCategory(MatchCategory matchCategory) {
    this.categories.put(matchCategory.getCategory(), matchCategory);
  }

  boolean hasAllFeaturesSolved() {
    return features.values().stream().allMatch(MatchFeature::hasValue);
  }

  boolean hasAllCategoryValues() {
    return categories.values().stream().allMatch(MatchCategory::hasValue);
  }

  void setSolution(String solution, String reason) {
    this.solution = solution;
    this.reason = reason;
  }


  boolean isSolved() {
    return solution != null;
  }

  boolean hasSolvedFeature(String featureName) {
    if (!features.containsKey(featureName)) {
      log.warn("[Solving] There is no feature {} request for match {}", featureName, this.matchId);
      return true;
    }
    return features.get(featureName).hasValue();
  }

  public String getMatchName() {
    return ResourceName.create("").add("matches", matchId).getPath();
  }

  public void updateCommentInput() {
    // TODO: implement it!
  }
}
