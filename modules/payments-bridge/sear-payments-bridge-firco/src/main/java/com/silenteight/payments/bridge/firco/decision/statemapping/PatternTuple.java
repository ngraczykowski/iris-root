package com.silenteight.payments.bridge.firco.decision.statemapping;

import lombok.Value;

@Value
class PatternTuple {

  String dataCenter;
  String sourceState;
  String unit;
  String recommendation;
  String destinationState;
}
