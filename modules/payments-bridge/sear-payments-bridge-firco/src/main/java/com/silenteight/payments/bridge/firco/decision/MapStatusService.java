package com.silenteight.payments.bridge.firco.decision;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.firco.decision.decisionmapping.StateMappingStrategySelector;
import com.silenteight.payments.bridge.firco.decision.statemapping.StateMappingStrategy;
import com.silenteight.payments.bridge.firco.decision.statemapping.StateMappingStrategy.MapStateInput;
import com.silenteight.payments.bridge.firco.decision.statemapping.StateMappingStrategy.MapStateOutput;
import com.silenteight.payments.bridge.firco.dto.input.StatusInfoDto;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
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
    log.warn("No destination status found for the request: {}. The selected replacement : {}",
        request, invalidStatusInfo.getName());
    return DestinationStatus.createInvalid(invalidStatusInfo);
  }

  private static String mapState(
      StateMappingStrategy stateMappingStrategy, MapStateInput mapStateInput) {

    return stateMappingStrategy.mapState(mapStateInput, () -> INVALID).getDestinationState();
  }
}
