package com.silenteight.connector.ftcc.callback.decision.decisionmode;

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
