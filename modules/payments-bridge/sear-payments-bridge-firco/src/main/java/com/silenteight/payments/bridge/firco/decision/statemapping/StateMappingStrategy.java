package com.silenteight.payments.bridge.firco.decision.statemapping;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

public interface StateMappingStrategy {

  String getName();

  MapStateOutput mapState(MapStateInput request);

  @Value
  @Builder
  class MapStateInput {

    @NonNull
    String sourceState;

    @NonNull
    String unit;

    @NonNull
    String dataCenter;

    @NonNull
    String recommendedAction;
  }

  @Value
  class MapStateOutput {

    @NonNull
    String destinationState;
  }
}
