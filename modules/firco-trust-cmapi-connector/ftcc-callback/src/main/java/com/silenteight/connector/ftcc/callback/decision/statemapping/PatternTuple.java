package com.silenteight.connector.ftcc.callback.decision.statemapping;

import lombok.Value;

@Value
class PatternTuple {

  String dataCenter;
  String sourceState;
  String unit;
  String recommendation;
  String destinationState;
}
