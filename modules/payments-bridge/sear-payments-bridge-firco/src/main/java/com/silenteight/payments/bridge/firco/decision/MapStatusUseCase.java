package com.silenteight.payments.bridge.firco.decision;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.firco.decision.decisionmapping.StateMappingStrategySelector;
import com.silenteight.payments.bridge.firco.decision.statemapping.StateMappingStrategy;
import com.silenteight.payments.bridge.firco.decision.statemapping.StateMappingStrategy.MapStateInput;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class MapStatusUseCase {

  private final StateMappingStrategySelector selector;

  @SuppressWarnings("FeatureEnvy")
  DestinationStatus mapStatus(MapStatusRequest request) {
    var stateMappingStrategy = selector.selectStrategy(request.getUnit());
    var nextStatusName = mapState(stateMappingStrategy, request.toMapStateInput());

    return request
        .findNextStatus(nextStatusName)
        .map(DestinationStatus::createValid)
        .orElseThrow();
  }

  private static String mapState(
      StateMappingStrategy stateMappingStrategy, MapStateInput mapStateInput) {

    return stateMappingStrategy.mapState(mapStateInput).getDestinationState();
  }
}
