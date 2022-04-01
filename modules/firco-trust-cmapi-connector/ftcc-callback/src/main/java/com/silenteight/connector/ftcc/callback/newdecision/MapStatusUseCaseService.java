package com.silenteight.connector.ftcc.callback.newdecision;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.connector.ftcc.common.dto.input.StatusInfoDto;

@RequiredArgsConstructor
@Slf4j
public class MapStatusUseCaseService implements MapStatusUseCase {

  private final DecisionConfigurationHolder decisionConfigurationHolder;

  @Override
  public DestinationStatus mapStatus(MapStatusRequest request) {
    var currentStatusName = request.getCurrentStatusName();
    var recommendation = request.getRecommendedAction();

    var decisionTransitions = decisionConfigurationHolder.getDecisionTransitions();
    var destinationState = decisionTransitions
        .stream()
        .filter(decisionTransition -> haveDecision(currentStatusName, recommendation,
            decisionTransition))
        .map(DecisionTransition::getDestinationState)
        .findAny()
        .orElseThrow(() -> new IllegalStateException(
            "No 'DestinationState' in configuration for currentStatusName=" + currentStatusName
                + " and S8 recommendedAction=" + recommendation));

    return DestinationStatus.builder()
        .status(buildStatus(request, destinationState))
        .build();
  }

  private static boolean haveDecision(
      String currentStatusName, String recommendation, DecisionTransition decisionTransition) {
    return decisionTransition.getSourceState().equals(currentStatusName) && decisionTransition
        .getRecommendation()
        .equals(recommendation);
  }

  private static StatusInfoDto buildStatus(MapStatusRequest request, String destinationState) {
    var statusInfoDto = request.findNextStatus(destinationState).orElseThrow();
    return statusInfoDto.toBuilder().build();
  }

}
