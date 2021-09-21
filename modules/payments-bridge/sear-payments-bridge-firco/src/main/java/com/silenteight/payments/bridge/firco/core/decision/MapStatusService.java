package com.silenteight.payments.bridge.firco.core.decision;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.firco.core.decision.decisionmapping.StateMappingStrategySelector;
import com.silenteight.payments.bridge.firco.core.decision.statemapping.StateMappingStrategy;
import com.silenteight.payments.bridge.firco.core.decision.statemapping.StateMappingStrategy.MapStateInput;
import com.silenteight.payments.bridge.firco.core.decision.statemapping.StateMappingStrategy.MapStateOutput;
import com.silenteight.payments.bridge.firco.dto.common.StatusInfoDto;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class MapStatusService implements MapStatusUseCase {

  private static final MapStateOutput INVALID = new MapStateOutput("");

  private final StateMappingStrategySelector selector;

  @Override
  @SuppressWarnings("FeatureEnvy")
  public DestinationStatus mapStatus(MapStatusRequest request) {
    var stateMappingStrategy = selector.selectStrategy(request.getUnit());
    var nextStatusName = mapState(stateMappingStrategy, request.toMapStateInput());

    return request
        .findNextStatus(nextStatusName)
        .map(DestinationStatus::createValid)
        .orElseGet(() -> createInvalid(request));
  }

  private DestinationStatus createInvalid(MapStatusRequest request) {
    /*
     * This is only a temporary code (hack) to eliminate the risk of the wrong configuration of the
     * decision engine. It must be deleted once we have collected all the necessary data about the
     * customer's transitions.
     */
    var invalidStatusInfo =
        request.getNextStatuses().isEmpty() ?
          new StatusInfoDto(null, request.getCurrentStatusName(), null, null) :
          request.getNextStatuses().iterator().next();
    return DestinationStatus.createInvalid(invalidStatusInfo);
  }

  private static String mapState(
      StateMappingStrategy stateMappingStrategy, MapStateInput mapStateInput) {

    return stateMappingStrategy.mapState(mapStateInput, () -> INVALID).getDestinationState();
  }
}
