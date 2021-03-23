package com.silenteight.serp.governance.policy.solve;

import lombok.NonNull;
import lombok.Value;

import java.util.Collection;
import java.util.Map;
import javax.annotation.concurrent.ThreadSafe;

import static java.util.List.copyOf;

@Value
@ThreadSafe
class FeatureLogic {

  int count;

  @NonNull
  Collection<MatchCondition> features;

  FeatureLogic(int count, @NonNull Collection<MatchCondition> features) {
    this.count = count;
    this.features = copyOf(features);
  }

  public boolean match(Map<String, String> featureValuesByName) {
    return getFeatures()
        .stream()
        .filter(feature -> feature.matchesFeatureValues(featureValuesByName))
        .limit(getCount())
        .count() == getCount();
  }
}
