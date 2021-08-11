package com.silenteight.payments.bridge.firco.decision;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DecisionFacade {

  private final MapStatusUseCase mapStatusUseCase;

  public DestinationStatus mapStatus(MapStatusRequest request) {
    return mapStatusUseCase.mapStatus(request);
  }
}
