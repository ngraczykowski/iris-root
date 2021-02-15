package com.silenteight.serp.governance.policy.solve;

import lombok.*;

import java.util.Collection;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class FeatureLogic {

  private int count;

  @NonNull
  private Collection<MatchCondition> features;

  public boolean match(Map<String, String> featureValuesByName) {
    return getFeatures()
        .stream()
        .filter(feature -> feature.matchesFeatureValues(featureValuesByName))
        .limit(getCount())
        .count() == getCount();
  }
}
