package com.silenteight.payments.bridge.firco.core.decision;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.firco.core.decision.decisionmapping.StateMappingStrategySelector;
import com.silenteight.payments.bridge.firco.core.decision.statemapping.StateMappingStrategy;
import com.silenteight.payments.bridge.firco.core.decision.statemapping.StateMappingStrategy.MapStateInput;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class MapStatusService implements MapStatusUseCase {

  private final StateMappingStrategySelector selector;

  @Override
  @SuppressWarnings("FeatureEnvy")
  public DestinationStatus mapStatus(MapStatusRequest request) {
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
