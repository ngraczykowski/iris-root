package com.silenteight.payments.bridge.firco.decision.decisionmode;

import lombok.Value;

@Value
class UnitPatternDecisionTuple {

  String unitPattern;
  DecisionMode mode;

  @Override
  public String toString() {
    return "{'" + unitPattern + "' -> " + mode + '}';
  }
}
