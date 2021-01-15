package com.silenteight.serp.governance.policy.solve;

import lombok.*;

import com.silenteight.serp.governance.policy.domain.Condition;

import org.apache.commons.lang3.NotImplementedException;

import java.util.Collection;
import java.util.Map;

import static com.silenteight.serp.governance.policy.domain.Condition.IS;
import static com.silenteight.serp.governance.policy.domain.Condition.IS_NOT;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class MatchCondition {

  @NonNull
  private String name;

  @NonNull
  private Condition condition;

  @NonNull
  private Collection<String> values;

  boolean matchesFeatureValues(Map<String, String> featureValuesByName) {
    String featureName = getName();

    if (condition.equals(IS))
      return checkIsCondition(featureValuesByName, featureName);
    if (condition.equals(IS_NOT))
      return !checkIsCondition(featureValuesByName, featureName);

    throw new NotImplementedException();
  }

  private boolean checkIsCondition(Map<String, String> featureValuesByName, String featureName) {
    if (featureValuesByName.containsKey(featureName))
      return getValues().contains(featureValuesByName.get(featureName));
    else
      return false;
  }
}
