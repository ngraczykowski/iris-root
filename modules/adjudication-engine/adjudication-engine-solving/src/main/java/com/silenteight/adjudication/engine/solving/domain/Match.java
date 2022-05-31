package com.silenteight.adjudication.engine.solving.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.common.resource.ResourceName;
import com.silenteight.adjudication.engine.solving.data.MatchAggregate;

import java.io.Serializable;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
@Slf4j
public class Match implements Serializable {

  private static final long serialVersionUID = 4207894003659221746L;
  private final long matchId;
  private final String clientMatchId;
  private final Map<String, MatchFeature> features;
  private final Map<String, MatchCategory> categories;
  String solution = null;
  String reason;

  public Match(long alertId, MatchAggregate matchAggregate) {
    matchId = matchAggregate.matchId();
    clientMatchId = matchAggregate.clientMatchId();
    features = matchAggregate.features().entrySet().stream()
        .collect(Collectors.toMap(Map.Entry::getKey,
            e -> new MatchFeature(alertId, matchId, clientMatchId, e.getValue())));
    categories = matchAggregate.categories().entrySet().stream()
        .collect(Collectors.toMap(Map.Entry::getKey,
            e -> new MatchCategory(alertId, matchId, e.getValue())));
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

  public String getMatchName(long alertId) {
    return ResourceName.create("")
        .add("alerts", alertId)
        .add("matches", matchId).getPath();
  }
}
