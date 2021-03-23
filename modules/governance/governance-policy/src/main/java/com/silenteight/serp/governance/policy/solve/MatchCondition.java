package com.silenteight.serp.governance.policy.solve;

import lombok.NonNull;
import lombok.Value;

import com.silenteight.serp.governance.policy.domain.Condition;

import org.apache.commons.lang3.NotImplementedException;

import java.util.Collection;
import java.util.Map;
import javax.annotation.concurrent.ThreadSafe;

import static com.silenteight.serp.governance.policy.domain.Condition.IS;
import static com.silenteight.serp.governance.policy.domain.Condition.IS_NOT;
import static java.util.List.copyOf;

@Value
@ThreadSafe
class MatchCondition {

  @NonNull
  String name;

  @NonNull
  Condition condition;

  @NonNull
  Collection<String> values;

  MatchCondition(
      @NonNull String name, @NonNull Condition condition, @NonNull Collection<String> values) {
    this.name = name;
    this.condition = condition;
    this.values = copyOf(values);
  }

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
