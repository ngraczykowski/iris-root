package com.silenteight.serp.governance.policy.solve;

import lombok.*;

import java.util.Collection;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class Feature {

  @NonNull
  private String name;

  @NonNull
  private Collection<String> values;

  boolean matchesFeatureValues(Map<String, String> featureValuesByName) {
    String featureName = getName();

    if (featureValuesByName.containsKey(featureName))
      return getValues().contains(featureValuesByName.get(featureName));
    else
      return false;
  }
}
