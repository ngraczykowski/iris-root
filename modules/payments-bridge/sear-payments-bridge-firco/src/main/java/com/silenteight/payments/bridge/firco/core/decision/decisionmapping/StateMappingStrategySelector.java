package com.silenteight.payments.bridge.firco.core.decision.decisionmapping;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.firco.core.decision.decisionmode.DecisionMode;
import com.silenteight.payments.bridge.firco.core.decision.decisionmode.DecisionModeResolver;
import com.silenteight.payments.bridge.firco.core.decision.statemapping.StateMappingStrategy;

import java.util.Map;

@RequiredArgsConstructor
public class StateMappingStrategySelector {

  private final DecisionModeResolver resolver;
  private final Map<DecisionMode, StateMappingStrategy> mappingStrategies;

  @NonNull
  public StateMappingStrategy selectStrategy(@NonNull String unit) {
    var decisionMode = resolver.resolve(unit);

    if (!mappingStrategies.containsKey(decisionMode)) {
      throw new IllegalStateException("No strategy selected for decision mode " + decisionMode);
    }

    return mappingStrategies.get(decisionMode);
  }
}
